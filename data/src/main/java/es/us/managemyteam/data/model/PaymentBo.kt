package es.us.managemyteam.data.model

data class PaymentBo(
    val status: String? = null,
    var userUuid: String? = null,
    val moment: String? = null,
    val quantity: String? = null,
    val concept: String? = null,
    val paymentId: String? = null
) : BaseBo()

data class PaypalConfigBo(
    val recipient: String? = null,
    val address: String? = null,
    val postcode: String? = null,
    val province: String? = null,
    val city: String? = null
)