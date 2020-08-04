package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.*
import es.us.managemyteam.R
import es.us.managemyteam.constant.RegistrationError
import es.us.managemyteam.data.model.Role
import es.us.managemyteam.extension.isEmail
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.PostRegistrationUc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel(
    private val postRegistrationUc: PostRegistrationUc
) : ViewModel() {

    private val createUser = MediatorLiveData<Resource<Boolean>>()
    private var createUserSource: LiveData<Resource<Boolean>> = MutableLiveData()

    fun getCreateUserData() = createUser

    fun createUser(
        email: String,
        password: String,
        confirmPassword: String,
        name: String,
        surname: String,
        phoneNumber: String,
        role: Role
    ) = viewModelScope.launch {
        if (validateForm(email, password, confirmPassword, name, surname, phoneNumber)) {
            createUser.removeSource(createUserSource)
            withContext(Dispatchers.IO) {
                createUserSource =
                    postRegistrationUc(email, password, name, surname, phoneNumber, role)
            }

            createUser.addSource(createUserSource) {
                createUser.value = it
            }
        }
    }

    private fun validateForm(
        email: String,
        password: String,
        passwordRepeated: String,
        name: String,
        surname: String,
        phoneNumber: String
    ): Boolean {
        return when {
            name.isBlank() || password.isBlank() || passwordRepeated.isBlank() || name.isBlank() || surname.isBlank() || phoneNumber.isBlank() -> {
                createUser.value = Resource.error(
                    Error(
                        R.string.registration_error_empty_fields,
                        RegistrationError.EMPTY_FIELDS.ordinal
                    )
                )
                false
            }
            !email.isEmail() -> {
                createUser.value = Resource.error(
                    Error(
                        R.string.registration_error_invalid_email,
                        RegistrationError.NOT_AN_EMAIL.ordinal
                    )
                )
                false
            }
            password.isBlank() || password != passwordRepeated -> {
                createUser.value = Resource.error(
                    Error(
                        R.string.registration_error_invalid_password,
                        RegistrationError.PASSWORDS_NOT_FILL.ordinal
                    )
                )
                false
            }
            else -> true
        }
    }
}