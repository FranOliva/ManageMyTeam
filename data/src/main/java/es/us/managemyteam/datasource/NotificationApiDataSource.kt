package es.us.managemyteam.datasource

import es.us.managemyteam.data.server.NotificationBodyBo
import es.us.managemyteam.data.server.NotificationDataBo
import es.us.managemyteam.data.ws.NotificationWs

interface NotificationApiDataSource {

    suspend fun sendNotification(
        deviceId: String,
        title: String,
        message: String
    ): Boolean

}

class NotificationApiDataSourceImpl(
    private val notificationWs: NotificationWs
) : NotificationApiDataSource {

    override suspend fun sendNotification(
        deviceId: String,
        title: String,
        message: String
    ) =
        notificationWs.sendNotification(
            body = NotificationBodyBo(
                deviceId,
                NotificationDataBo.new(title, message)
            )
        ).isSuccess()

}