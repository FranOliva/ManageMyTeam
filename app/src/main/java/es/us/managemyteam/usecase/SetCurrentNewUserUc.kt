package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.RegistrationBo
import es.us.managemyteam.repository.UserRepository

class SetCurrentNewUserUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(registration: RegistrationBo?) {
        userRepository.setCurrentRegistration(registration)
    }

}