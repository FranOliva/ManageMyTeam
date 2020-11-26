package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class LogoutUc(private val userRepository: UserRepository) {

    suspend operator fun invoke() {
        userRepository.logout()
    }

}