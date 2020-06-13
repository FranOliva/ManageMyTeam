package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.*
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetEventsUc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventsViewModel(
    private val getEventsUc: GetEventsUc
) : ViewModel() {

    private val events: MediatorLiveData<Resource<List<EventBo>>> = MediatorLiveData()
    private var eventsSource: LiveData<Resource<List<EventBo>>> = MutableLiveData()

    init {
        getEvents()
    }

    fun getEvents() {
        viewModelScope.launch(Dispatchers.Main) {
            events.value = Resource.loading()
            events.removeSource(eventsSource)
            withContext(Dispatchers.IO) {
                eventsSource = getEventsUc()
            }
            events.addSource(eventsSource) {
                events.value = it
            }
        }
    }

    fun getEventsData(): LiveData<Resource<List<EventBo>>> {
        return events
    }


}