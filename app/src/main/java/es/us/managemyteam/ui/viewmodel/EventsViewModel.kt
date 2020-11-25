package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetEventsUc
import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventsViewModel(
    private val getEventsUc: GetEventsUc,
    getUserUc: GetUserUc
) : BaseLoggedViewModel(getUserUc) {

    private val events: CustomMediatorLiveData<Resource<List<EventBo>>> = CustomMediatorLiveData()

    fun getEvents() {
        viewModelScope.launch(Dispatchers.Main) {
            events.setData(Resource.loading())
            withContext(Dispatchers.IO) {
                events.changeSource(Dispatchers.Main, getEventsUc(false))
            }
        }
    }

    fun getEventsData(): LiveData<Resource<List<EventBo>>> {
        return events.liveData()
    }


}