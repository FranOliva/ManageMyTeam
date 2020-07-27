package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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

    fun getUserByUid(uid: String): LiveData<Resource<UserBo>>

    suspend fun login(
        email: String,
        password: String
    ): LiveData<Resource<UserBo>>
}

class UserRepositoryImpl : UserRepository {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val createUserData = MutableLiveData<Resource<Boolean>>()
    private val userData = MutableLiveData<Resource<UserBo>>()
    private val userTable = RepositoryUtil.getDatabaseTable(DatabaseTables.USER_TABLE)

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
                val exception = (it.exception as FirebaseAuthException).errorCode
                createUserData.value =
                    Resource.error(Error(serverErrorMessage = it.exception?.localizedMessage))
            }
        }

        return createUserData
    }

    override suspend fun login(email: String, password: String): LiveData<Resource<UserBo>> {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                auth.currentUser?.let { firebaseUser ->
                    getUserByUid(firebaseUser.uid)
                }
            }
        }
        return userData
    }

    override fun getUserByUid(uid: String): LiveData<Resource<UserBo>> {
        userTable.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                userData.value = Resource.error(Error(serverErrorMessage = error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserBo::class.java)
                userData.value = Resource.success(user)
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

}
