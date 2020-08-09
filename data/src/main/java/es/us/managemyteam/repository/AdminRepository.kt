package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.us.managemyteam.data.database.DatabaseTables
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.RepositoryUtil
import es.us.managemyteam.repository.util.Resource

interface AdminRepository {

    suspend fun getPlayersNotEnabled(): LiveData<Resource<List<UserBo>>>

    suspend fun acceptPlayer(player: UserBo): LiveData<Resource<Boolean>>

    suspend fun rejectPlayer(player: UserBo): LiveData<Resource<Boolean>>
}

class AdminRepositoryImpl : AdminRepository {

    private val userTable = RepositoryUtil.getDatabaseTable(DatabaseTables.USER_TABLE)
    private val playersData = MutableLiveData<Resource<List<UserBo>>>()
    private val playerNotEnabledData = MutableLiveData<Resource<Boolean>>()

    override suspend fun getPlayersNotEnabled(): LiveData<Resource<List<UserBo>>> {
        playersData.postValue(null)
        userTable.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                playersData.value = Resource.error(Error(serverErrorMessage = error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val playersNotEnabled =
                    snapshot.children.mapNotNull { it.getValue(UserBo::class.java) }
                        .filter { it.isPlayer() && it.enable != null && !it.enable }
                playersData.value =
                    Resource.success(playersNotEnabled)
            }

        })

        return playersData
    }

    override suspend fun acceptPlayer(player: UserBo): LiveData<Resource<Boolean>> {
        playerNotEnabledData.postValue(null)
        player.id?.let {
            userTable.child(it).setValue(true) { error, _ ->
                playerNotEnabledData.value = if (error != null) {
                    Resource.error(Error(serverErrorMessage = error.message))
                } else {
                    Resource.success(true)
                }
            }
        }
        return playerNotEnabledData
    }

    override suspend fun rejectPlayer(player: UserBo): LiveData<Resource<Boolean>> {
        playerNotEnabledData.postValue(null)
        player.id?.let {
            userTable.child(it).setValue(true) { error, _ ->
                playerNotEnabledData.value = if (error != null) {
                    Resource.error(Error(serverErrorMessage = error.message))
                } else {
                    Resource.success(false)
                }
            }
        }
        return playerNotEnabledData
    }

}