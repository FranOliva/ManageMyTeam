package es.us.managemyteam.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            // TODO show notification
            Log.i("Noti", message.data.toString())
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}