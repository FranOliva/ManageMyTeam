package es.us.managemyteam.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import es.us.managemyteam.databinding.FragmentSplashBinding
import es.us.managemyteam.ui.view.CustomToolbar

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: CustomToolbar) {
        toolbar.apply {
            setNavAction { findNavController().navigateUp() }
            setTitle("Splash")
        }
    }
}