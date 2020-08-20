package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.viewModelScope
import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.usecase.LogoutUc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuViewModel(getUserUc: GetUserUc, private val logoutUc: LogoutUc) :
    BaseLoggedViewModel(getUserUc) {

    init {
        getUser()
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                logoutUc()
            }
        }
    }
}