package es.us.managemyteam.data.server

import com.google.gson.annotations.SerializedName

data class NotificationBodyBo(
    @SerializedName("to") val deviceInstanceId: String,
    @SerializedName("data") val data: NotificationDataBo
)

data class NotificationDataBo(
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String
) {
    companion object {
        fun new(title: String = "", message: String = "") = NotificationDataBo(title, message)
    }
}

data class NotificationResponseBo(
    val failure: Int = 0,
    val success: Int = 0
) {
    fun isSuccess() = success != 0
}