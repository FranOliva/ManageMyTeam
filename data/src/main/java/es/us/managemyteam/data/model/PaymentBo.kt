package es.us.managemyteam.data.model

data class PaymentBo(
    val status: String? = null,
    val userUuid: String? = null,
    val moment: String? = null,
    val quantity: String? = null,
    val concept: String? = null
) : BaseBo()