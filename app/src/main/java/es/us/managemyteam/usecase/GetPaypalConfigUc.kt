package es.us.managemyteam.usecase

import es.us.managemyteam.repository.PaymentRepository

class GetPaypalConfigUc(private val paymentRepository: PaymentRepository) {

    suspend operator fun invoke() = paymentRepository.getPaypalConfig()
}