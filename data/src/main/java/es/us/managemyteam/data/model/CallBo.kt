package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CallBo(
    val called: List<UserCalledBo> = listOf(),
    val notCalled: List<UserCalledBo> = listOf(),
    val date: Date = Date()
) : BaseBo(), Parcelable
