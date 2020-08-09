package es.us.managemyteam.contract

interface AcceptPlayerListener {

    fun onPlayerAccepted(uuid: String)

    fun onPlayerRefused(uuid: String)
}