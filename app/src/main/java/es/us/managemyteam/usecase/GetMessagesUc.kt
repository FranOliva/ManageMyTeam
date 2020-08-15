package es.us.managemyteam.usecase

import es.us.managemyteam.repository.ChatRepository

class GetMessagesUc(private val chatRepository: ChatRepository) {

    suspend operator fun invoke() = chatRepository.getMessages()

}