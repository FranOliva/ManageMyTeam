package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.contract.BaseAdapterClickListener
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.FragmentEventsBinding
import es.us.managemyteam.extension.setNavIcon
import es.us.managemyteam.extension.setToolbarTitle
import es.us.managemyteam.extension.show
import es.us.managemyteam.extension.showErrorDialog
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.adapter.EventsAdapter
import es.us.managemyteam.ui.viewmodel.EventsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class EventsFragment : BaseFragment<FragmentEventsBinding>(), BaseAdapterClickListener<EventBo> {

    private var eventsAdapter: EventsAdapter? = null
    private val eventsViewModel: EventsViewModel by viewModel()
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPressed()
        setupList()
        setupClickListeners()
        setupEventsObserver()
        setupUserObserver()

    }

    private fun setupBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun setupUserObserver() {
        eventsViewModel.getUserData()
            .observe(viewLifecycleOwner, object : ResourceObserver<UserBo>() {
                override fun onSuccess(response: UserBo?) {
                    response?.let {
                        eventsViewModel.getEvents()
                        setupButtonCreateEventVisibility(!it.isPlayer())
                    }
                }
            })
        eventsViewModel.getUser()
    }

    private fun setupButtonCreateEventVisibility(visible: Boolean) {
        viewBinding.eventsFabCreateEvent.visibility = if (visible) {
            VISIBLE
        } else {
            GONE
        }
    }

    private fun setupClickListeners() {
        viewBinding.eventsFabCreateEvent.setOnClickListener {
            findNavController().navigate(R.id.action_events_to_create_event)
        }
    }

    private fun setupEventsObserver() {
        eventsViewModel.getEventsData()
            .observe(viewLifecycleOwner, object : ResourceObserver<List<EventBo>>() {
                override fun onSuccess(response: List<EventBo>?) {
                    response?.let {
                        eventsAdapter?.setData(it.sortedBy { event -> event.date })
                        eventsAdapter?.notifyDataSetChanged()

                        if (it.isNotEmpty()) {
                            viewBinding.eventsEmptyResults.root.visibility = GONE
                        } else {
                            viewBinding.eventsEmptyResults.root.visibility = VISIBLE
                        }
                    }
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(
                        getString(error.errorMessageId)
                    )
                }

                override fun onLoading(loading: Boolean) {
                    super.onLoading(loading)
                    showLoader(loading)
                }
            })

    }

    private fun setupList() {
        viewBinding.eventsList.apply {
            if (eventsAdapter == null) {
                eventsAdapter = EventsAdapter(this@EventsFragment)
            }
            eventsAdapter?.let {
                adapter = it
            }
        }
    }

    override fun onAdapterItemClicked(item: EventBo, position: Int) {
        findNavController().navigate(
            R.id.action_events_to_detail, bundleOf(
                Pair(getString(R.string.navigation_event__uuid__argument), item.uuid)
            )
        )
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEventsBinding {
        return FragmentEventsBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.events))
            setNavIcon(null)
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

}