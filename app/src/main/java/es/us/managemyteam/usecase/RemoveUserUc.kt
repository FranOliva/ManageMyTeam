package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class RemoveUserUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(uuid: String) = userRepository.removeUser(uuid)
}