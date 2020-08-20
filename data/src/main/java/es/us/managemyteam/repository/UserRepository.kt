package es.us.managemyteam.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.us.managemyteam.data.R
import es.us.managemyteam.data.database.DatabaseTables
import es.us.managemyteam.data.model.RegistrationBo
import es.us.managemyteam.data.model.Role
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.PasswordUtil
import es.us.managemyteam.repository.util.RepositoryUtil
import es.us.managemyteam.repository.util.Resource

interface UserRepository {

    suspend fun createUser(
        email: String,
        password: String,
        name: String,
        surname: String,
        phoneNumber: String,
        role: Role
    ): LiveData<Resource<Boolean>>

    suspend fun getCurrentRegistration(): RegistrationBo?

    suspend fun setCurrentRegistration(registration: RegistrationBo?)

    suspend fun getUserByUid(uid: String): LiveData<Resource<UserBo>>

    suspend fun login(
        email: String,
        password: String
    ): LiveData<Resource<String>>

    suspend fun logout()

    suspend fun removeUser(uuid: String): LiveData<Resource<Boolean>>

    suspend fun updateUserData(
        name: String,
        surname: String,
        phoneNumber: String,
        age: Int?,
        dorsal: Long?
    ): LiveData<Resource<Boolean>>

    suspend fun updateEmail(currentPassword: String, email: String): LiveData<Resource<Boolean>>

    suspend fun updatePassword(
        currentPassword: String,
        password: String
    ): LiveData<Resource<Boolean>>

    suspend fun areTermsChecked(): Boolean

    suspend fun setTermsChecked(checked: Boolean)
}

class UserRepositoryImpl : UserRepository {

    private var user: UserBo? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val createUserData = MutableLiveData<Resource<Boolean>>()
    private val userData = MutableLiveData<Resource<UserBo>>()
    private val userTable = RepositoryUtil.getDatabaseTable(DatabaseTables.USER_TABLE)
    private val loginData = MutableLiveData<Resource<String>>()
    private val removeUserData = MutableLiveData<Resource<Boolean>>()
    private val updateUserData = MutableLiveData<Resource<Boolean>>()
    private val updateEmailData = MutableLiveData<Resource<Boolean>>()
    private val updatePasswordData = MutableLiveData<Resource<Boolean>>()
    private var currentUser: RegistrationBo? = RegistrationBo()
    private var termsChecked = false


    override suspend fun createUser(
        email: String,
        password: String,
        name: String,
        surname: String,
        phoneNumber: String,
        role: Role
    ): LiveData<Resource<Boolean>> {
        val hashedPassword = PasswordUtil.hashPassword(password)

        auth.createUserWithEmailAndPassword(email, hashedPassword).addOnCompleteListener {
            if (it.isSuccessful) {
                createUserFirebaseDatabase(name, surname, email, phoneNumber, role)
            } else {
                val exception = (it.exception as FirebaseAuthException)
                createUserData.value =
                    Resource.error(Error(errorMessageId = getMessageErrorByErrorCode(exception)))
            }
        }

        return createUserData
    }

    override suspend fun login(email: String, password: String): LiveData<Resource<String>> {
        loginData.postValue(null)
        val hashedPassword = PasswordUtil.hashPassword(password)

        auth.signInWithEmailAndPassword(email, hashedPassword).addOnCompleteListener {
            if (it.isSuccessful) {
                loginData.value = Resource.success(auth.currentUser?.uid)
            } else {
                val exception = getMessageErrorByErrorCode(it.exception as FirebaseAuthException)
                Log.e("LOGIN", "Login error: $exception")
                loginData.value =
                    Resource.error(Error(errorMessageId = exception))
            }
        }
        return loginData
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun getCurrentRegistration(): RegistrationBo? {
        return currentUser
    }

    override suspend fun setCurrentRegistration(user: RegistrationBo?) {
        currentUser = user
    }

    override suspend fun removeUser(uuid: String): LiveData<Resource<Boolean>> {
        removeUserData.postValue(null)
        userTable.child(uuid).removeValue { error, _ ->
            removeUserData.value = if (error != null) {
                Resource.error(Error(serverErrorMessage = error.message))
            } else {
                Resource.success(false)
            }
        }
        return removeUserData
    }

    override suspend fun updateUserData(
        name: String,
        surname: String,
        phoneNumber: String,
        age: Int?,
        dorsal: Long?
    ): LiveData<Resource<Boolean>> {
        val currentUser = auth.currentUser
        updateUserData.postValue(null)
        if (currentUser != null) {
            userTable.child(currentUser.uid).updateChildren(
                mapOf(
                    Pair("name", name),
                    Pair("surname", surname),
                    Pair("phoneNumber", phoneNumber),
                    Pair("age", age),
                    Pair("dorsal", dorsal)
                )
            )
            updateUserData.postValue(Resource.success(true))
        } else {
            updateUserData.postValue(Resource.error(Error(R.string.unknown_error)))
        }

        return updateUserData

    }

    override suspend fun updateEmail(
        currentPassword: String,
        email: String
    ): LiveData<Resource<Boolean>> {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val credentials = EmailAuthProvider.getCredential(
                currentUser.email!!,
                PasswordUtil.hashPassword(currentPassword)
            )
            currentUser.reauthenticate(credentials).addOnCompleteListener {
                if (it.isSuccessful) {
                    updateEmailFirebaseAuth(currentUser, email)
                } else {
                    updateEmailData.value =
                        Resource.error(Error(errorMessageId = R.string.login_error_wrong_password))
                }
            }
        } else {
            showGenericError(updateEmailData)
        }
        return updateEmailData
    }

    private fun updateEmailFirebaseAuth(currentUser: FirebaseUser, email: String) {
        updateEmailData.postValue(null)
        currentUser.updateEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                updateEmailDatabase(currentUser.uid, email)
            } else {
                showGenericError(updateEmailData)
            }
        }
    }

    private fun updateEmailDatabase(userUuid: String?, email: String) {
        if (userUuid != null) {
            userTable.child(userUuid).child("email").setValue(email) { error, _ ->
                updateEmailData.value = if (error == null) {
                    Resource.success(true)
                } else {
                    Resource.error(Error(serverErrorMessage = error.message))
                }
            }
        } else {
            updateEmailData.postValue(Resource.error(Error(R.string.unknown_error)))
        }
    }

    override suspend fun updatePassword(
        currentPassword: String,
        password: String
    ): LiveData<Resource<Boolean>> {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val credentials = EmailAuthProvider.getCredential(
                currentUser.email!!,
                PasswordUtil.hashPassword(currentPassword)
            )
            currentUser.reauthenticate(credentials).addOnCompleteListener {
                if (it.isSuccessful) {
                    val hashedPassword = PasswordUtil.hashPassword(password)
                    updatePassword(currentUser, hashedPassword)
                } else {
                    updatePasswordData.value =
                        Resource.error(Error(errorMessageId = R.string.login_error_wrong_password))
                }
            }
        } else {
            showGenericError(updatePasswordData)
        }
        return updatePasswordData
    }

    override suspend fun areTermsChecked(): Boolean {
        return termsChecked
    }

    override suspend fun setTermsChecked(checked: Boolean) {
        termsChecked = checked
    }

    private fun updatePassword(currentUser: FirebaseUser, hashedPassword: String) {
        updatePasswordData.postValue(null)
        currentUser.updatePassword(hashedPassword)
            .addOnCompleteListener { passwordTask ->
                if (passwordTask.isSuccessful) {
                    updatePasswordData.value = Resource.success(true)
                } else {
                    showGenericError(updatePasswordData)
                }
            }
    }

    private fun showGenericError(liveData: MutableLiveData<Resource<Boolean>>) {
        liveData.value = Resource.error(Error(R.string.unknown_error))
    }

    override suspend fun getUserByUid(uid: String): LiveData<Resource<UserBo>> {
        userData.postValue(null)
        userTable.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                userData.value = Resource.error(Error(serverErrorMessage = error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(UserBo::class.java)
                val userEnabled = user?.enable == true
                val userStaffFirstTime = user?.isStaff() == true && user?.enable == false
                if (userEnabled || userStaffFirstTime) {
                    userData.value = Resource.success(user)
                } else {
                    userData.value =
                        Resource.error(Error(errorMessageId = R.string.login_error_user_not_activated))
                }
            }
        })

        return userData
    }

    private fun createUserFirebaseDatabase(
        name: String,
        surname: String,
        email: String,
        phoneNumber: String,
        role: Role
    ): LiveData<Resource<Boolean>> {
        val user =
            UserBo(name, surname, email, phoneNumber, null, role, null, false).apply {
                uuid = auth.currentUser?.uid
            }

        userTable
            .child(FirebaseAuth.getInstance().currentUser?.uid!!).setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    userTable
                        .child(auth.currentUser?.uid!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                createUserData.value =
                                    Resource.error(Error(serverErrorMessage = error.message))
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                createUserData.value = Resource.success(true)
                            }

                        })
                }
                auth.signOut()
            }

        return createUserData
    }

    private fun getMessageErrorByErrorCode(exception: FirebaseAuthException): Int {
        return when (exception.errorCode) {
            "ERROR_WEAK_PASSWORD" -> R.string.registration_error_weak_password
            "ERROR_EMAIL_ALREADY_IN_USE" -> R.string.registration_error_email_already_used
            "ERROR_USER_NOT_FOUND" -> R.string.login_error_user_not_found
            "ERROR_WRONG_PASSWORD" -> R.string.login_error_wrong_password
            else -> R.string.unknown_error
        }
    }

}
