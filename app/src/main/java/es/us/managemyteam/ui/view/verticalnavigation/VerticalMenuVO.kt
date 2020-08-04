package es.us.managemyteam.ui.view.verticalnavigation

import android.content.Context
import es.us.managemyteam.R

data class VerticalMenuVO(val id: VerticalMenuId, val title: String, val icon: Int) {

    companion object {
        fun getDefaultMenu(context: Context): List<VerticalMenuVO> {
            val verticalMenuList = mutableListOf<VerticalMenuVO>()

            val administration = VerticalMenuVO(
                VerticalMenuId.ADMINISTRATION_ID,
                "Administración del club",
                R.drawable.ic_settings
            )
            val logout =
                VerticalMenuVO(VerticalMenuId.LOGOUT_ID, "Cerrar sesión", R.drawable.ic_settings)

            verticalMenuList.add(administration)
            verticalMenuList.add(logout)

            return verticalMenuList
        }
    }
}
