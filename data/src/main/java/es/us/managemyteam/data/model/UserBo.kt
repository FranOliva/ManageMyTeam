package es.us.managemyteam.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserBo(
    var name: String? = null,
    var surname: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    val age: Int? = null,
    val role: Role? = null,
    val dorsal: Long? = null,
    val enable: Boolean? = null
) : BaseBo(), Parcelable {

    fun getFullName() = (name ?: "").plus(" ").plus(surname ?: "")

    fun isAdmin() = Role.ADMIN == role

    fun isPlayer() = Role.PLAYER == role

    fun isStaff() = Role.STAFF == role

}

enum class Role {
    ADMIN,
    STAFF,
    PLAYER
}