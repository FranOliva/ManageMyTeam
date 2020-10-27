package es.us.managemyteam.usecase

import es.us.managemyteam.repository.EventRepository

class GetEventsUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(pastEvents: Boolean) = eventRepository.getEvents(pastEvents)

}