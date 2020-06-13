package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.repository.EventRepository
import es.us.managemyteam.repository.util.Resource

class GetCurrentNewEventUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(): LiveData<Resource<EventBo>> {
        return MutableLiveData<Resource<EventBo>>().apply {
            postValue(Resource.success(eventRepository.getCurrentNewEvent()))
        }
    }

}