package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.MessageBo
import es.us.managemyteam.repository.ChatRepository
import java.util.*

class PostMessageUc(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(newMessage: String, from: String, fromName: String) =
        chatRepository.postMessage(
            MessageBo(newMessage, from, fromName, Date())
        )

}