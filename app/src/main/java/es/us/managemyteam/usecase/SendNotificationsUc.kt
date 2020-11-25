package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import es.us.managemyteam.repository.NotificationRepository
import es.us.managemyteam.repository.UserRepository
import es.us.managemyteam.repository.util.Resource

class SendNotificationsUc(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        title: String,
        message: String,
        vararg userIds: String
    ): LiveData<Resource<Boolean>> {
        return notificationRepository.sendNotifications(title, message, *userIds)
    }


}