package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserBo(
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val age: Int? = null,
    val role: Role? = null,
    val dorsal: Long? = null,
    val enable: Boolean? = null
) : BaseBo(), Parcelable {

    fun isAdmin() = Role.ADMIN == role

    fun isPlayer() = Role.PLAYER == role

    fun isStaff() = Role.STAFF == role
}

enum class Role {
    ADMIN,
    STAFF,
    PLAYER
}