package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.viewModelScope
import es.us.managemyteam.R
import es.us.managemyteam.extension.isEmail
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.usecase.UpdateEmailUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateEmailViewModel(
    getUserUc: GetUserUc,
    private val updateEmailUc: UpdateEmailUc
) : BaseLoggedViewModel(getUserUc) {

    private val updateEmail = CustomMediatorLiveData<Resource<Boolean>>()

    fun getUpdateEmailData() = updateEmail.liveData()

    fun getUpdateEmail(email: String) {
        viewModelScope.launch(Dispatchers.Main) {
            if (validateForm(email)) {
                updateEmail.setData(Resource.loading(data = null))
                withContext(Dispatchers.IO) {
                    updateEmail.changeSource(
                        Dispatchers.Main,
                        updateEmailUc(email)
                    )
                }
            }
        }
    }

    private fun validateForm(email: String): Boolean {
        return when {
            !email.isEmail() -> {
                updateEmail.setData(
                    Resource.error(
                        Error(
                            R.string.registration_error_invalid_email
                        )
                    )
                )
                false
            }

            else -> true
        }
    }
}