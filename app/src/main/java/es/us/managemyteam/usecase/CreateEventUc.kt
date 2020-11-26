package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import es.us.managemyteam.data.model.CallBo
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.data.model.LocationBo
import es.us.managemyteam.repository.EventRepository
import es.us.managemyteam.repository.util.Resource
import java.util.*

class CreateEventUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(
        title: String,
        date: Date?,
        type: String,
        description: String?,
        location: LocationBo?,
        call: CallBo?
    ): LiveData<Resource<EventBo>> {

        return eventRepository.createEvent(
            title,
            date,
            type,
            description,
            location,
            call
        )

    }

}