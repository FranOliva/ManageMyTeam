package es.us.managemyteam.repository.util

const val ERROR_UNKNOWN = -1

data class Error(
    var errorMessageId: Int = ERROR_UNKNOWN,
    val errorActionId: Int = ERROR_UNKNOWN,
    val throwable: Throwable? = null,
    val serverErrorMessage: String? = null
) {

    fun isUnknown() : Boolean {
        return errorMessageId == ERROR_UNKNOWN
    }
}