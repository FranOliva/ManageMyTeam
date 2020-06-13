package es.us.managemyteam.repository.util

import com.google.firebase.database.FirebaseDatabase

object RepositoryUtil {

    fun getDatabaseTable(tableName: String) = FirebaseDatabase.getInstance().getReference(tableName)
}