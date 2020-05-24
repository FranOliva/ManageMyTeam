package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.us.managemyteam.data.model.LocationBo
import es.us.managemyteam.repository.EventRepository
import es.us.managemyteam.repository.util.Resource

class GetEventLocationUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(): LiveData<Resource<LocationBo?>> {
        return MutableLiveData<Resource<LocationBo?>>().apply {
            postValue(Resource.success(eventRepository.getEventLocation()))
        }
    }

}