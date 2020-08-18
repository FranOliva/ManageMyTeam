package es.us.managemyteam.usecase

import com.paypal.android.sdk.payments.ProofOfPayment
import es.us.managemyteam.mapper.PaymentMapper
import es.us.managemyteam.repository.PaymentRepository

class CreatePaymentUc(private val paymentRepository: PaymentRepository) {

    suspend operator fun invoke(concept: String, quantity: String, paymentProof: ProofOfPayment) =
        paymentRepository.createPayment(
            PaymentMapper.paymentToBo(concept, quantity, paymentProof)
        )
}