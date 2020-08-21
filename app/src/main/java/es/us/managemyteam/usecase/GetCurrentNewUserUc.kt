package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.us.managemyteam.data.model.RegistrationBo
import es.us.managemyteam.repository.UserRepository
import es.us.managemyteam.repository.util.Resource

class GetCurrentNewUserUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(): LiveData<Resource<RegistrationBo>> {
        return MutableLiveData<Resource<RegistrationBo>>().apply {
            postValue(Resource.success(userRepository.getCurrentRegistration()))
        }
    }

}