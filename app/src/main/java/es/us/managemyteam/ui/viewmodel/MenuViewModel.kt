package es.us.managemyteam.ui.viewmodel

import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.util.FirebaseAuthUtil

class MenuViewModel(getUserUc: GetUserUc) : BaseLoggedViewModel(getUserUc) {

    private val auth = FirebaseAuthUtil.getFirebaseAuthInstance()

    fun logout() {
        auth.signOut()
    }
}