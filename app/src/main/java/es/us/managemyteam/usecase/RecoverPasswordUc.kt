package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class RecoverPasswordUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String) = userRepository.recoverPassword(email)
}