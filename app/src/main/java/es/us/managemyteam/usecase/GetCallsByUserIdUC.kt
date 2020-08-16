package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.CallStatus
import es.us.managemyteam.repository.CallRepository

class GetCallsByUserIdUC(private val callRepository: CallRepository) {

    suspend operator fun invoke(userUuid: String, status: CallStatus) =
        callRepository.getCallsByUserId(userUuid, status)

}