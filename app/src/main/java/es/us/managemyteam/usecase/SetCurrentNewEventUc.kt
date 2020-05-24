package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.repository.EventRepository

class SetCurrentNewEventUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(event: EventBo?) {
        eventRepository.setCurrentNewEvent(event)
    }

}