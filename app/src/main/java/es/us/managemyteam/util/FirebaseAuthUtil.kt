package es.us.managemyteam.util

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthUtil {

    fun getFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}