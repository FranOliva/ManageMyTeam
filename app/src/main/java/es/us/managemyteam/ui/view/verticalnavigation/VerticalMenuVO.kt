package es.us.managemyteam.ui.view.verticalnavigation

import android.content.Context

data class VerticalMenuVO(val id: VerticalMenuId, val title: String, val icon: Int) {

    companion object {
        fun getDefaultMenu(context: Context): List<VerticalMenuVO> {
            val verticalMenuList = mutableListOf<VerticalMenuVO>()


            return verticalMenuList
        }
    }
}
