package es.us.managemyteam.repository

import com.google.android.gms.maps.model.LatLng

interface EventRepository {

    suspend fun getEventLocation(): LatLng?

    suspend fun setEventLocation(latLng: LatLng?)

}

class EventRepositoryImpl : EventRepository {

    private var eventLocation: LatLng? = null

    override suspend fun getEventLocation(): LatLng? {
        return eventLocation
    }

    override suspend fun setEventLocation(latLng: LatLng?) {
        this.eventLocation = latLng
    }

}