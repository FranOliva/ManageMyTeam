package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.databinding.FragmentPaypalConfigBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.ui.viewmodel.AdminPaypalViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AdminPaypalConfigFragment : BaseFragment<FragmentPaypalConfigBinding>() {

    private val adminPaypalViewModel: AdminPaypalViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        adminPaypalViewModel.getPaypalConfigData().observe(viewLifecycleOwner, object :)
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPaypalConfigBinding {
        return FragmentPaypalConfigBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle("Configuración de Paypal")
            setNavIcon(ContextCompat.getDrawable(context, R.drawable.ic_back))
            setNavAction { popBack() }
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.hide()
    }
}