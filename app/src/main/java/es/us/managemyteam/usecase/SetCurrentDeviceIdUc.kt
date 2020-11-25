package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class SetCurrentDeviceIdUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(deviceId: String) =
        userRepository.updateDeviceInstanceIdDatabase(deviceId)
}