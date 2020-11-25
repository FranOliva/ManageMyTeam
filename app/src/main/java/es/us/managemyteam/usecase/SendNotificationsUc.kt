package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import es.us.managemyteam.repository.NotificationRepository
import es.us.managemyteam.repository.UserRepository
import es.us.managemyteam.repository.util.Resource

class SendNotificationsUc(
    private val notificationRepository: NotificationRepository) {

    suspend operator fun invoke(
        title: String,
        message: String,
        vararg userIds: Pair<String, String>
    ): LiveData<Resource<Boolean>> {
        return notificationRepository.sendNotifications(title, message, *userIds)
    }


}