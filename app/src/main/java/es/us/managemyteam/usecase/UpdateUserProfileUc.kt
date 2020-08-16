package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class UpdateUserProfileUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(
        name: String,
        surname: String,
        phoneNumber: String,
        age: Int?,
        dorsal: Long?
    ) = userRepository.updateUserData(name, surname, phoneNumber, age, dorsal)
}