package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
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

    suspend fun login() {

    }
}

class UserRepositoryImpl : UserRepository {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val createUserData = MutableLiveData<Resource<Boolean>>()
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
            }
        }

        return createUserData
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
            }

        return createUserData
    }

}
