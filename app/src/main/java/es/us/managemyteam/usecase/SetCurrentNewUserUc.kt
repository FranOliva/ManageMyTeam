package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.UserRepository

class SetCurrentNewUserUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(user: UserBo?) {
        userRepository.setCurrentNewUser(user)
    }

}