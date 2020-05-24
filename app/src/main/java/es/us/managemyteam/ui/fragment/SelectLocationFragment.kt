package es.us.managemyteam.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.databinding.FragmentSelectLocationBinding
import es.us.managemyteam.extension.hide

class SelectLocationFragment : BaseFragment<FragmentSelectLocationBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectLocationBinding {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupToolbar(toolbar: Toolbar) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.hide()
    }
}