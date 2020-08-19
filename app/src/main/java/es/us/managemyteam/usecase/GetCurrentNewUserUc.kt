package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.UserRepository
import es.us.managemyteam.repository.util.Resource

class GetCurrentNewUserUc(private val userRepository: UserRepository) {

    suspend operator fun invoke(): LiveData<Resource<UserBo>> {
        return MutableLiveData<Resource<UserBo>>().apply {
            postValue(Resource.success(userRepository.getCurrentNewUser()))
        }
    }

}