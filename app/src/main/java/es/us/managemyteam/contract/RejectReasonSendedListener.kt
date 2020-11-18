package es.us.managemyteam.contract

interface RejectReasonSendedListener {

    fun onRejectReasonSended(uuid: String, observation: String)
}