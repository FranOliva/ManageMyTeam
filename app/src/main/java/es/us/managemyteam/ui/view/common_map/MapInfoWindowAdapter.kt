package es.us.managemyteam.ui.view.common_map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import es.us.managemyteam.databinding.ViewInfoWindowBinding

class MapInfoWindowAdapter(
    context: Context,
    private val markers: Collection<MarkerItemVo>
) : GoogleMap.InfoWindowAdapter {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getInfoContents(marker: Marker?): View {
        val item = markers.find {
            it.position == marker?.position
        }
        val viewBinding = ViewInfoWindowBinding.inflate(inflater, null, false)

        viewBinding.infoWindowLabelTitle.apply {
            text = item?.title
            visibility = if (item?.title.isNullOrEmpty()) {
                GONE
            } else {
                VISIBLE
            }
        }
        viewBinding.infoWindowLabelSubtitle.apply {
            text = item?.subtitle
            visibility = if (item?.subtitle.isNullOrEmpty()) {
                GONE
            } else {
                VISIBLE
            }
        }

        return viewBinding.root
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }
}