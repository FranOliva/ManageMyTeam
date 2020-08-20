package es.us.managemyteam.usecase

import es.us.managemyteam.repository.PaymentRepository

class GetMyPaymentsUc(private val paymentsRepository: PaymentRepository) {

    suspend operator fun invoke() = paymentsRepository.getMyPayments()
}