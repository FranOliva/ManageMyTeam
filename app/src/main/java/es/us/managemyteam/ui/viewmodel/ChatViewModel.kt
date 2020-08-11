package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.data.model.MessageBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetMessagesUc
import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.usecase.PostMessageUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val getMessagesUc: GetMessagesUc,
    private val postMessageUc: PostMessageUc,
    getUserUc: GetUserUc
) : BaseLoggedViewModel(getUserUc) {

    private val messages: CustomMediatorLiveData<Resource<List<MessageBo>>> =
        CustomMediatorLiveData()
    private val messageCreated: CustomMediatorLiveData<Resource<Boolean>> =
        CustomMediatorLiveData()

    init {
        getMessages()
    }

    fun getMessages() {
        viewModelScope.launch(Dispatchers.Main) {
            messages.setData(Resource.loading(data = null))
            withContext(Dispatchers.IO) {
                messages.changeSource(
                    Dispatchers.Main,
                    getMessagesUc()
                )
            }
        }
    }


    fun postMessage(message: String, from: String) {
        viewModelScope.launch(Dispatchers.Main) {
            messageCreated.setData(Resource.loading(data = null))
            withContext(Dispatchers.IO) {
                messageCreated.changeSource(
                    Dispatchers.Main,
                    postMessageUc(message, from)
                )
            }
        }
    }

    fun getMessagesData(): LiveData<Resource<List<MessageBo>>> {
        return messages.apply { setData(null) }.liveData()
    }

    fun getMessageCreatedData(): LiveData<Resource<Boolean>> {
        return messageCreated.apply { setData(null) }.liveData()
    }

}