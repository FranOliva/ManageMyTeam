package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserCalledBo(
    val user: UserBo,
    var enable: Boolean = false,
    val observation: String? = null
) : BaseBo(), Parcelable