package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.us.managemyteam.data.model.CallBo
import es.us.managemyteam.repository.EventRepository
import es.us.managemyteam.repository.util.Resource

class GetCurrentCallUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(): LiveData<Resource<CallBo>> {
        return MutableLiveData<Resource<CallBo>>().apply {
            postValue(Resource.success(eventRepository.getCurrentCall()))
        }
    }

}