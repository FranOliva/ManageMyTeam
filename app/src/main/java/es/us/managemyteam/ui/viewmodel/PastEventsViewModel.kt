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

class PastEventsViewModel(
    private val getEventsUc: GetEventsUc,
    getUserUc: GetUserUc
) : BaseLoggedViewModel(getUserUc) {

    private val pastEvents: CustomMediatorLiveData<Resource<List<EventBo>>> =
        CustomMediatorLiveData()

    init {
        getPastEvents()
    }

    private fun getPastEvents() {
        viewModelScope.launch(Dispatchers.Main) {
            pastEvents.setData(Resource.loading())
            withContext(Dispatchers.IO) {
                pastEvents.changeSource(Dispatchers.Main, getEventsUc(true))
            }
        }
    }

    fun getPastEventsData(): LiveData<Resource<List<EventBo>>> {
        return pastEvents.liveData()
    }


}