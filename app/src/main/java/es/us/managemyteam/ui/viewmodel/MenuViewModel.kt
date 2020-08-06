package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import es.us.managemyteam.util.FirebaseAuthUtil

class MenuViewModel : ViewModel() {

    private val auth = FirebaseAuthUtil.getFirebaseAuthInstance()

    fun logout() {
        auth.signOut()
    }
}