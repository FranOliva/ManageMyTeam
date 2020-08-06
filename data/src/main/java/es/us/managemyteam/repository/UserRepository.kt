package es.us.managemyteam.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.us.managemyteam.data.R
import es.us.managemyteam.data.database.DatabaseTables
import es.us.managemyteam.data.model.Role
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.util.Error
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

    suspend fun getUserByUid(uid: String): LiveData<Resource<UserBo>>

    suspend fun login(
        email: String,
        password: String
    ): LiveData<Resource<String>>

    suspend fun logout()
}

class UserRepositoryImpl : UserRepository {

    private var user: UserBo? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val createUserData = MutableLiveData<Resource<Boolean>>()
    private val userData = MutableLiveData<Resource<UserBo>>()
    private val userTable = RepositoryUtil.getDatabaseTable(DatabaseTables.USER_TABLE)
    private val loginData = MutableLiveData<Resource<String>>()

    override suspend fun createUser(
        email: String,
        password: String,
        name: String,
        surname: String,
        phoneNumber: String,
        role: Role
    ): LiveData<Resource<Boolean>> {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
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
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
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

    override suspend fun getUserByUid(uid: String): LiveData<Resource<UserBo>> {
        userData.postValue(null)
        userTable.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                userData.value = Resource.error(Error(serverErrorMessage = error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(UserBo::class.java)
                if (user?.enable == true) {
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
            UserBo(name, surname, email, phoneNumber, null, role, null, false)

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
