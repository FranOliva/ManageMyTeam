package es.us.managemyteam.usecase

import es.us.managemyteam.repository.CallRepository

class AcceptCallUc(private val callRepository: CallRepository) {

    suspend operator fun invoke(uuid: String, userUuid: String, fromRejection: Boolean) =
        if (fromRejection) {
            callRepository.acceptCallFromRejection(uuid, userUuid)
        } else {
            callRepository.acceptCallFromPending(uuid, userUuid)
        }

}