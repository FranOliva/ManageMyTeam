package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.databinding.FragmentAdministrationBinding
import es.us.managemyteam.extension.*

class AdministrationFragment : BaseFragment<FragmentAdministrationBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        //viewBinding.administratorContainerCreateCoach.setOnClickListener {
        //    findNavController().navigate(R.id.)
        //}

        viewBinding.administratorContainerAcceptsPlayer.setOnClickListener {
            findNavController().navigate(R.id.action_administration_to_accept_player)
        }

        //viewBinding.administratorContainerEditProfile.setOnClickListener {
        //    findNavController().navigate(R.id.action_edit_user_to_edit_password)
        //}

        //viewBinding.editUserImgBack.setOnClickListener {
        //   popBack()
        //}
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdministrationBinding {
        return FragmentAdministrationBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.edit_club_title))
            setNavIcon(ContextCompat.getDrawable(context, R.drawable.ic_back))
            setNavAction { popBack() }
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.hide()
    }
}