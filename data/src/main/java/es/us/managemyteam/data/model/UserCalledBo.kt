package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserCalledBo(
    val userId: String = "",
    val userName: String = "",
    var enable: Boolean = false,
    val observation: String = ""
) : BaseBo(), Parcelable