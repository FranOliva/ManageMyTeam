package es.us.managemyteam.repository.util

import com.google.firebase.database.FirebaseDatabase

object RepositoryUtil {

    private fun getDatabase() = FirebaseDatabase.getInstance()

    fun getDatabaseTable(tableName: String) = getDatabase().getReference(tableName)
}