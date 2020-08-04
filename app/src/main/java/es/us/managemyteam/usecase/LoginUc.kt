package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class LoginUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, password: String) =
        userRepository.login(email, password)
}