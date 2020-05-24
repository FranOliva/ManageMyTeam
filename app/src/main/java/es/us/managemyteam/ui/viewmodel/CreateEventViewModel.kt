package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.*
import es.us.managemyteam.R
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.data.model.LocationBo
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CreateEventViewModel(
    private val getEventLocationUc: GetEventLocationUc,
    private val setEventLocationUc: SetEventLocationUc,
    private val getCurrentNewEventUc: GetCurrentNewEventUc,
    private val setCurrentNewEventUc: SetCurrentNewEventUc,
    private val createEventUc: CreateEventUc
) : ViewModel() {

    private val currentNewEvent = MediatorLiveData<Resource<EventBo>>()
    private var currentNewEventSource: LiveData<Resource<EventBo>> = MutableLiveData()

    private val createEvent = MediatorLiveData<Resource<Boolean>>()
    private var createEventSource: LiveData<Resource<Boolean>> = MutableLiveData()

    private val locationSelected = MediatorLiveData<Resource<LocationBo?>>()
    private var locationSelectedSource: LiveData<Resource<LocationBo?>> = MutableLiveData()

    fun createEvent(
        title: String,
        date: Date?,
        type: String,
        description: String?,
        location: LocationBo?,
        assistants: List<UserBo>?
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            if (validateForm(title, date, type)) {

                createEvent.removeSource(createEventSource)
                withContext(Dispatchers.IO) {
                    createEventSource =
                        createEventUc(title, date, type, description, location, assistants)
                }

                createEvent.addSource(createEventSource) {
                    createEvent.value = it
                }

            }
        }

    fun createEventData(): LiveData<Resource<Boolean>> {
        createEvent.value = null
        createEvent.removeSource(createEventSource)
        return createEvent
    }

    private fun validateForm(
        title: String,
        date: Date?,
        type: String
    ): Boolean {
        return when {
            title.isBlank() -> {
                createEvent.value = Resource.error(
                    Error(R.string.create_event_error_title)
                )
                false
            }
            date == null -> {
                createEvent.value = Resource.error(
                    Error(R.string.create_event_error_date)
                )
                false
            }
            type.isBlank() -> {
                createEvent.value = Resource.error(
                    Error(R.string.create_event_error_event_type)
                )
                false
            }
            else -> true
        }
    }

    fun getCurrentNewEvent() =
        viewModelScope.launch(Dispatchers.Main) {
            currentNewEvent.value = Resource.loading()
            currentNewEvent.removeSource(currentNewEventSource)
            withContext(Dispatchers.IO) {
                currentNewEventSource = getCurrentNewEventUc()
            }
            currentNewEvent.addSource(currentNewEventSource) {
                currentNewEvent.value = it
            }
        }

    fun getCurrentNewEventData() = currentNewEvent as LiveData<Resource<EventBo>>

    fun setCurrentNewEvent(event: EventBo) =
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                setCurrentNewEventUc(event)
            }
        }

    fun clearEvent() =
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                setCurrentNewEventUc(null)
                setLocationSelected(null)
            }
        }

    fun getLocationSelected() =
        viewModelScope.launch(Dispatchers.Main) {
            locationSelected.value = Resource.loading()
            locationSelected.removeSource(locationSelectedSource)
            withContext(Dispatchers.IO) {
                locationSelectedSource = getEventLocationUc()
            }
            locationSelected.addSource(locationSelectedSource) {
                locationSelected.value = it
            }
        }

    fun getLocationSelectedData() = locationSelected as LiveData<Resource<LocationBo?>>

    fun setLocationSelected(location: LocationBo?) =
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                setEventLocationUc(location)
            }
        }
}