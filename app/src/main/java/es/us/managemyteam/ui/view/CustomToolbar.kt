package es.us.managemyteam.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import es.us.managemyteam.databinding.ViewCustomToolbarBinding

class CustomToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding =
        ViewCustomToolbarBinding.inflate(LayoutInflater.from(context), this, false)

    fun getToolbar() = viewBinding.mainToolbar

    fun setTitle(title: String?) {
        viewBinding.mainLabelToolbarTitle.text = title
    }

    //region Nav icon method
    fun setNavIcon(iconDrawable: Drawable?) {
        getToolbar().navigationIcon = iconDrawable
    }

    fun setNavAction(listener: (View) -> Unit) {
        getToolbar().setNavigationOnClickListener(listener)
    }
    //endregion

}