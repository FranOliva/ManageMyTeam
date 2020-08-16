package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetEventDetailUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventDetailViewModel(
    private val getEventDetailUc: GetEventDetailUc
) : ViewModel() {

    private val eventDetail: CustomMediatorLiveData<Resource<EventBo>> = CustomMediatorLiveData()

    fun getEventDetail(uuid: String) {
        viewModelScope.launch(Dispatchers.Main) {
            eventDetail.setData(Resource.loading(data = null))
            withContext(Dispatchers.IO) {
                eventDetail.changeSource(
                    Dispatchers.Main,
                    getEventDetailUc(uuid)
                )
            }
        }
    }

    fun getEventDetailData(): LiveData<Resource<EventBo>> {
        return eventDetail.liveData()
    }

}