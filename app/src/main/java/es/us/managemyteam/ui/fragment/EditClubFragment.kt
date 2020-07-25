package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.View
import es.us.managemyteam.databinding.FragmentEditClubBinding
import es.us.managemyteam.ui.view.common_map.MapListener

class EditClubFragment : BaseFragment<FragmentEditClubBinding>(), MapListener {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMap(savedInstanceState)
        setupClickListeners()
        setupObservers()
    }


}