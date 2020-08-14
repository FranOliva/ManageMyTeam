package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class UpdateEmailUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String) = userRepository.updateEmail(email)
}