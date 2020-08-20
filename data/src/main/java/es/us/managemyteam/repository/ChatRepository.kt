package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.us.managemyteam.data.database.DatabaseTables
import es.us.managemyteam.data.model.MessageBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.RepositoryUtil
import es.us.managemyteam.repository.util.Resource

interface ChatRepository {

    suspend fun getMessages(): LiveData<Resource<List<MessageBo>>>

    suspend fun postMessage(newMessage: MessageBo): LiveData<Resource<Boolean>>

}

class ChatRepositoryImpl : ChatRepository {

    private val chatRef = RepositoryUtil.getDatabaseTable(DatabaseTables.MESSAGE_TABLE)
    private val chat = MutableLiveData<Resource<List<MessageBo>>>()
    private val chatCreated = MutableLiveData<Resource<Boolean>>()

    init {
        initializeChatListener()
    }

    override suspend fun getMessages(): LiveData<Resource<List<MessageBo>>> {
        return chat
    }

    override suspend fun postMessage(newMessage: MessageBo): LiveData<Resource<Boolean>> {
        chatRef.push().setValue(
            newMessage
        ) { databaseError, _ ->
            if (databaseError != null) {
                chatCreated.value =
                    Resource.error(Error(serverErrorMessage = databaseError.message))
            } else {
                chatCreated.value = Resource.success(true)
            }
        }
        return chatCreated
    }

    private fun initializeChatListener() {
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                chat.value = Resource.error(Error(serverErrorMessage = databaseError.message))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chat.value =
                    Resource.success(dataSnapshot.children.mapNotNull { it.getValue(MessageBo::class.java) }
                        .sortedBy { it.date.time })
            }

        })
    }

}