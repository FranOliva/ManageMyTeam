package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class GetUserUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(uid: String) =
        userRepository.getUserByUid(uid)
}