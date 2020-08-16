package es.us.managemyteam.ui.view.verticalnavigation

import android.content.Context
import es.us.managemyteam.R

data class VerticalMenuVO(val id: VerticalMenuId, val title: String, val icon: Int) {

    companion object {
        fun getDefaultMenu(
            context: Context,
            isAdmin: Boolean,
            isStaff: Boolean
        ): List<VerticalMenuVO> {
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
            val team = VerticalMenuVO(
                VerticalMenuId.MY_TEAM_ID,
                context.resources.getString(R.string.my_team_title),
                R.drawable.ic_team
            )
            val logout = VerticalMenuVO(
                VerticalMenuId.LOGOUT_ID,
                "Cerrar sesión",
                R.drawable.ic_settings
            )

            verticalMenuList.add(club)
            if (isAdmin) {
                verticalMenuList.add(administration)
            }

            if (isAdmin || isStaff) {
                verticalMenuList.add(team)
            }

            verticalMenuList.add(logout)


            return verticalMenuList
        }
    }
}
