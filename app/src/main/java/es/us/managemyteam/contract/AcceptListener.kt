package es.us.managemyteam.contract

interface AcceptListener {

    fun onAccepted(uuid: String)

    fun onRefused(uuid: String)
}