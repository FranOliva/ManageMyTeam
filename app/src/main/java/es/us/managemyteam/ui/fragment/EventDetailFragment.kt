package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.databinding.FragmentEventDetailBinding
import es.us.managemyteam.extension.hide
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.EventDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class EventDetailFragment : BaseFragment<FragmentEventDetailBinding>() {

    private val eventDetailViewModel: EventDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(getString(R.string.navigation_event__uuid__argument))?.let {
            setupEventDetailObserver(it)
        }
    }

    private fun setupEventDetailObserver(uuid: String) {
        eventDetailViewModel.getEventDetailData()
            .observe(viewLifecycleOwner, object : ResourceObserver<EventBo>() {
                override fun onSuccess(response: EventBo?) {
                    response?.let {
                        viewBinding.eventDetailLabelTitle.text = it.title
                    }
                }

            })
        eventDetailViewModel.getEventDetail(uuid)

    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEventDetailBinding {
        return FragmentEventDetailBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.hide()
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.hide()
    }
}