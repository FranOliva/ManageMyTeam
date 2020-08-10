package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CallBo(
    val called: List<UserCalledBo>? = null,
    val notCalled: List<UserCalledBo>? = null,
    val date: Date? = null
) : BaseBo(), Parcelable
