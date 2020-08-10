package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.CallBo
import es.us.managemyteam.repository.EventRepository

class SetCurrentCallUc(private val eventRepository: EventRepository) {

    suspend operator fun invoke(call: CallBo?) {
        eventRepository.setCurrentCall(call)
    }

}