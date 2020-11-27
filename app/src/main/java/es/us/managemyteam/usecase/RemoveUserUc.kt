package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class RemoveUserUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(uuid: String, password: String? = null) =
        userRepository.removeUser(uuid, password)
}