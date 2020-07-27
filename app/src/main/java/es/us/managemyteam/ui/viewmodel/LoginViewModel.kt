package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.*
import es.us.managemyteam.R
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.extension.isEmail
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.LoginUc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val loginUc: LoginUc) : ViewModel() {

    private val user: MediatorLiveData<Resource<UserBo>> = MediatorLiveData()
    private var userSource: LiveData<Resource<UserBo>> = MutableLiveData()

    fun getLoginData(): LiveData<Resource<UserBo>> {
        user.value = null
        return user
    }

    fun login(
        email: String,
        password: String
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            if (validateForm(email, password)) {
                user.value = Resource.loading(null)
                user.removeSource(userSource)
                withContext(Dispatchers.IO) {
                    userSource =
                        loginUc(
                            email,
                            password
                        )
                }
                user.addSource(userSource) {
                    user.value = it
                }
            }
        }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            email.isBlank() || password.isBlank() -> {
                user.value = Resource.error(
                    Error(R.string.registration_error_empty_fields)
                )
                false
            }
            !email.isEmail() -> {
                user.value = Resource.error(
                    Error(R.string.registration_error_invalid_email)
                )
                false
            }
            else -> true
        }
    }
}