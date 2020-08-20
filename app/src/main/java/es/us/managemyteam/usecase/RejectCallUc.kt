package es.us.managemyteam.usecase

import es.us.managemyteam.repository.CallRepository

class RejectCallUc(private val callRepository: CallRepository) {

    suspend operator fun invoke(
        uuid: String,
        userUuid: String,
        observation: String,
        fromAcceptation: Boolean
    ) = if (fromAcceptation) {
        callRepository.rejectCallFromAcceptation(uuid, userUuid, observation)
    } else {
        callRepository.rejectCallFromPending(uuid, userUuid, observation)
    }

}