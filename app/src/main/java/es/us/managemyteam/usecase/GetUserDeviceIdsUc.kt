package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class GetUserDeviceIdsUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(vararg userIds: String) =
        userRepository.getUserDeviceIds(*userIds)

}