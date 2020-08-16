package es.us.managemyteam.ui.view.verticalnavigation

import android.content.Context
import es.us.managemyteam.R

data class VerticalMenuVO(val id: VerticalMenuId, val title: String, val icon: Int) {

    companion object {
        fun getDefaultMenu(context: Context, isAdmin: Boolean): List<VerticalMenuVO> {
            val verticalMenuList = mutableListOf<VerticalMenuVO>()

            val club = VerticalMenuVO(
                VerticalMenuId.MY_CLUB_ID,
                "Datos del club",
                R.drawable.ic_profile
            )
            val administration = VerticalMenuVO(
                VerticalMenuId.ADMINISTRATION_ID,
                "Administración del club",
                R.drawable.ic_settings
            )
            val logout = VerticalMenuVO(
                VerticalMenuId.LOGOUT_ID,
                "Cerrar sesión",
                R.drawable.ic_settings
            )

            val myPayments = VerticalMenuVO(
                VerticalMenuId.PAYMENTS_ID,
                "Mis pagos",
                R.drawable.ic_settings
            )

            verticalMenuList.add(club)
            if (isAdmin) {
                verticalMenuList.add(administration)
            }
            if (!isAdmin) {
                verticalMenuList.add(myPayments)
            }
            verticalMenuList.add(logout)


            return verticalMenuList
        }
    }
}
