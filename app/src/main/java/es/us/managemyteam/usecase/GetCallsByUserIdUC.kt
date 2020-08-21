package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.CallStatus
import es.us.managemyteam.repository.CallRepository

class GetCallsByUserIdUC(private val callRepository: CallRepository) {

    suspend operator fun invoke(userUuid: String, status: CallStatus) = when (status) {
        CallStatus.PENDING -> callRepository.getPendingCallsByUserId(userUuid)
        CallStatus.ACCEPTED -> callRepository.getAcceptedCallsByUserId(userUuid)
        else -> callRepository.getRejectedCallsByUserId(userUuid)
    }


}