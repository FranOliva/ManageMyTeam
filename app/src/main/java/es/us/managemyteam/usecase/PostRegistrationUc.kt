package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import es.us.managemyteam.data.model.Role
import es.us.managemyteam.repository.UserRepository
import es.us.managemyteam.repository.util.Resource

class PostRegistrationUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        surname: String,
        phoneNumber: String,
        role: Role
    ): LiveData<Resource<Boolean>> {
        return userRepository.createUser(email, password, name, surname, phoneNumber, role)
    }
}