package es.us.managemyteam.data.ws

import es.us.managemyteam.constant.Notification
import es.us.managemyteam.data.server.NotificationBodyBo
import es.us.managemyteam.data.server.NotificationResponseBo
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationWs {

    @Headers("Content-Type: application/json")
    @POST("fcm/send")
    suspend fun sendNotification(
        @Header("Authorization") firebaseKey: String = Notification.FIREBASE_CLOUD_ID,
        @Body body: NotificationBodyBo
    ): NotificationResponseBo
}