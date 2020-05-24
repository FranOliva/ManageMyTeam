package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import es.us.managemyteam.repository.EventRepository
import es.us.managemyteam.repository.util.Resource

class GetEventLocationUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(): LiveData<Resource<LatLng?>> {
        return MutableLiveData<Resource<LatLng?>>().apply {
            postValue(Resource.success(eventRepository.getEventLocation()))
        }
    }

}