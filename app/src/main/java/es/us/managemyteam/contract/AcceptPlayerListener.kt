package es.us.managemyteam.contract

import es.us.managemyteam.data.model.UserBo

interface AcceptPlayerListener {

    fun onPlayerAccepted(user: UserBo)

    fun onPlayerRefused(user: UserBo)
}