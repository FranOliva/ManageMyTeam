package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserCalledBo(
    val userId: String = "",
    val userName: String = "",
    var called: Boolean = false,
    var enable: Int = CallStatus.PENDING.ordinal,
    val observation: String = ""
) : BaseBo(), Parcelable

enum class CallStatus {
    PENDING,
    ACCEPTED,
    DENIED
}