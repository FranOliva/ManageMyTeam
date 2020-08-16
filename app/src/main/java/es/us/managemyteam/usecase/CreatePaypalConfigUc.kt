package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.PaypalConfigBo
import es.us.managemyteam.repository.PaymentRepository

class CreatePaypalConfigUc(private val paymentRepository: PaymentRepository) {

    suspend operator fun invoke(paypalConfig: PaypalConfigBo) =
        paymentRepository.createPaypalConfig(paypalConfig)
}