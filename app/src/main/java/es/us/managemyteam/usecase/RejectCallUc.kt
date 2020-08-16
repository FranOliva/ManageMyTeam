package es.us.managemyteam.usecase

import es.us.managemyteam.repository.CallRepository

class RejectCallUc(private val callRepository: CallRepository) {

    suspend operator fun invoke(uuid: String, userUuid: String, observation: String) =
        callRepository.rejectCall(uuid, userUuid, observation)

}