package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import es.us.managemyteam.data.model.CallStatus
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.AcceptCallUc
import es.us.managemyteam.usecase.GetCallsByUserIdUC
import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.usecase.RejectCallUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CallViewModel(
    getUserUc: GetUserUc,
    private val getCallsByUserIdUC: GetCallsByUserIdUC,
    private val acceptCallUc: AcceptCallUc,
    private val rejectCallUc: RejectCallUc
) : BaseLoggedViewModel(getUserUc) {

    private val pendingCalls = CustomMediatorLiveData<Resource<List<EventBo>>>()
    private val acceptedCalls = CustomMediatorLiveData<Resource<List<EventBo>>>()
    private val rejectedCalls = CustomMediatorLiveData<Resource<List<EventBo>>>()
    private val acceptCallData = CustomMediatorLiveData<Resource<Boolean>>()
    private val auth = FirebaseAuth.getInstance()

    init {
        getPendingCalls()
    }

    fun getPendingCalls() {
        viewModelScope.launch(Dispatchers.Main) {
            pendingCalls.setData(Resource.loading())
            withContext(Dispatchers.IO) {
                pendingCalls.changeSource(
                    Dispatchers.Main,
                    getCallsByUserIdUC(auth.currentUser?.uid ?: "", CallStatus.PENDING)
                )
            }
        }
    }

    fun getRejectedCalls() {
        viewModelScope.launch(Dispatchers.Main) {
            rejectedCalls.setData(Resource.loading())
            withContext(Dispatchers.IO) {
                rejectedCalls.changeSource(
                    Dispatchers.Main,
                    getCallsByUserIdUC(auth.currentUser?.uid ?: "", CallStatus.DENIED)
                )
            }
        }
    }

    fun getAcceptedCalls() {
        viewModelScope.launch(Dispatchers.Main) {
            acceptedCalls.setData(Resource.loading())
            withContext(Dispatchers.IO) {
                acceptedCalls.changeSource(
                    Dispatchers.Main,
                    getCallsByUserIdUC(auth.currentUser?.uid ?: "", CallStatus.ACCEPTED)
                )
            }
        }
    }

    fun getPendingCallsData(): LiveData<Resource<List<EventBo>>> {
        return pendingCalls.liveData()
    }

    fun getRejectedCallsData(): LiveData<Resource<List<EventBo>>> {
        return rejectedCalls.liveData()
    }

    fun getAcceptedCallsData(): LiveData<Resource<List<EventBo>>> {
        return acceptedCalls.liveData()
    }

    fun acceptCall(uuid: String) {
        viewModelScope.launch(Dispatchers.Main) {
            acceptCallData.setData(Resource.loading(data = null))
            withContext(Dispatchers.IO) {
                acceptCallData.changeSource(
                    Dispatchers.Main,
                    acceptCallUc(uuid, auth.currentUser?.uid ?: "")
                )
            }
        }
    }

    fun getAcceptCallData(): LiveData<Resource<Boolean>> {
        acceptCallData.setData(null)
        return acceptCallData.liveData()
    }

    fun rejectCall(uuid: String, observation: String) {
        viewModelScope.launch(Dispatchers.Main) {
            acceptCallData.setData(Resource.loading(data = null))
            withContext(Dispatchers.IO) {
                acceptCallData.changeSource(
                    Dispatchers.Main,
                    rejectCallUc(uuid, auth.currentUser?.uid ?: "", observation)
                )
            }
        }
    }

}