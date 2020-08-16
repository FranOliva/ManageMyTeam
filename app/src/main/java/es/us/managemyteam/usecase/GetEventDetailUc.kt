package es.us.managemyteam.usecase

import es.us.managemyteam.repository.EventRepository

class GetEventDetailUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(uuid: String) = eventRepository.getEventDetail(uuid)

}