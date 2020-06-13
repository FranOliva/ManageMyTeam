package es.us.managemyteam.util

import com.google.firebase.database.FirebaseDatabase

object DatabaseUtil {

    fun getDatabaseInstance() {
        FirebaseDatabase.getInstance().reference
    }
}