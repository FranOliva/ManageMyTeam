package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class UpdatePasswordUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(password: String) = userRepository.updatePassword(password)
}