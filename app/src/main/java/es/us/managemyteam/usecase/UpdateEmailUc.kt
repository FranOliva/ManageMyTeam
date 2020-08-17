package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class UpdateEmailUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(currentPassword: String, email: String) =
        userRepository.updateEmail(currentPassword, email)
}