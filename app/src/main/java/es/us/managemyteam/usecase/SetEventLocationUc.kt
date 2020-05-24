package es.us.managemyteam.usecase

import com.google.android.gms.maps.model.LatLng
import es.us.managemyteam.repository.EventRepository

class SetEventLocationUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(latLng: LatLng?) {
        return eventRepository.setEventLocation(latLng)
    }

}