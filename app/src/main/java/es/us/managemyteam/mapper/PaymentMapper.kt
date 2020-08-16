package es.us.managemyteam.mapper

import com.paypal.android.sdk.payments.ProofOfPayment
import es.us.managemyteam.data.model.PaymentBo
import es.us.managemyteam.util.DateUtil

object PaymentMapper {

    fun paymentToBo(concept: String, quantity: String, proofOfPayment: ProofOfPayment) =
        PaymentBo(
            proofOfPayment.state,
            moment = DateUtil.format(proofOfPayment.createTime),
            quantity = quantity,
            concept = concept
        )
}