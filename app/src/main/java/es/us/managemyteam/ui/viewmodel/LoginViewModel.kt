package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.*
import es.us.managemyteam.R
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.extension.isEmail
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.usecase.LoginUc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val loginUc: LoginUc, private val getUserUc: GetUserUc) : ViewModel() {

    private val login: MediatorLiveData<Resource<Boolean>> = MediatorLiveData()
    private var loginSource: LiveData<Resource<Boolean>> = MutableLiveData()
    private val user: MediatorLiveData<Resource<UserBo>> = MediatorLiveData()
    private var userSource: LiveData<Resource<UserBo>> = MutableLiveData()

    fun getLoginData(): LiveData<Resource<Boolean>> {
        login.value = null
        return login
    }

    fun login(
        email: String,
        password: String
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            if (validateForm(email, password)) {
                login.value = Resource.loading(null)
                login.removeSource(loginSource)
                withContext(Dispatchers.IO) {
                    loginSource =
                        loginUc(
                            email,
                            password
                        )
                }
                login.addSource(loginSource) {
                    login.value = it
                }
            }
        }

    fun getUserData(): LiveData<Resource<UserBo>> {
        user.value = null
        user.removeSource(userSource)
        return user
    }

    fun getUser(uid: String) =
        viewModelScope.launch(Dispatchers.Main) {
            user.value = Resource.loading(null)
            user.removeSource(userSource)
            withContext(Dispatchers.IO) {
                userSource = getUserUc(uid)

            }
            user.addSource(userSource) {
                user.value = it
            }
        }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            email.isBlank() || password.isBlank() -> {
                login.value = Resource.error(
                    Error(
                        R.string.registration_error_empty_fields
                    )
                )
                false
            }
            !email.isEmail() -> {
                login.value = Resource.error(
                    Error(
                        R.string.registration_error_invalid_email
                    )
                )
                false
            }
            else -> true
        }
    }
}