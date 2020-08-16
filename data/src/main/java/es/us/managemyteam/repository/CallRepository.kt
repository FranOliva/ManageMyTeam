package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.us.managemyteam.data.database.DatabaseTables
import es.us.managemyteam.data.model.CallStatus
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.data.model.UserCalledBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.RepositoryUtil
import es.us.managemyteam.repository.util.Resource

interface CallRepository {

    suspend fun getCallsByUserId(userUuid: String): LiveData<Resource<List<EventBo>>>

    suspend fun acceptCall(uuid: String, userUuid: String): LiveData<Resource<Boolean>>

    suspend fun rejectCall(
        uuid: String,
        userUuid: String,
        observation: String
    ): LiveData<Resource<Boolean>>
}

class CallRepositoryImpl : CallRepository {

    private val eventTable = RepositoryUtil.getDatabaseTable(DatabaseTables.EVENT_TABLE)
    private val callAcceptedData = MutableLiveData<Resource<Boolean>>()
    private val callsData = MutableLiveData<Resource<List<EventBo>>>()

    override suspend fun getCallsByUserId(userUuid: String): LiveData<Resource<List<EventBo>>> {
        callsData.postValue(null)
        eventTable.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                callsData.value = Resource.error(Error(serverErrorMessage = error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val calls =
                    snapshot.children.mapNotNull { it.getValue(EventBo::class.java) }
                        .filter { it.call?.called?.find { user -> user.userId == userUuid } != null }
                        .sortedBy {
                            it.date?.time ?: 0L
                        }
                callsData.value = Resource.success(calls)
            }

        })

        return callsData
    }

    override suspend fun acceptCall(uuid: String, userUuid: String): LiveData<Resource<Boolean>> {
        callAcceptedData.postValue(null)
        val callRef = eventTable.child(uuid).child("call").child("called")
        callRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                callAcceptedData.value =
                    Resource.error(error = Error(serverErrorMessage = error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val allUsers =
                    snapshot.children.mapNotNull { it.getValue(UserCalledBo::class.java) }
                val userToReject = allUsers.find { it.userId == userUuid }
                val index = allUsers.indexOf(userToReject)

                callRef.child(index.toString()).updateChildren(
                    mapOf(
                        Pair(
                            "enable", CallStatus.ACCEPTED.ordinal
                        )
                    )
                )

                callAcceptedData.value = Resource.success(true)

            }
        })

        return callAcceptedData
    }

    override suspend fun rejectCall(
        uuid: String,
        userUuid: String,
        observation: String
    ): LiveData<Resource<Boolean>> {
        callAcceptedData.postValue(null)
        val callRef = eventTable.child(uuid).child("call").child("called")
        callRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                callAcceptedData.value =
                    Resource.error(error = Error(serverErrorMessage = error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val allUsers =
                    snapshot.children.mapNotNull { it.getValue(UserCalledBo::class.java) }
                val userToReject = allUsers.find { it.userId == userUuid }
                val index = allUsers.indexOf(userToReject)

                callRef.child(index.toString()).updateChildren(
                    mapOf(
                        Pair(
                            "enable", CallStatus.DENIED.ordinal
                        ),
                        Pair("observation", observation)

                    )
                )

                callAcceptedData.value = Resource.success(true)

            }
        })

        return callAcceptedData
    }

}