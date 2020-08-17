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
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.EventDetailViewModel
import es.us.managemyteam.util.DateUtil
import es.us.managemyteam.util.PermissionUtil
import org.koin.android.viewmodel.ext.android.viewModel

private val PERMISSIONS = arrayOf(android.Manifest.permission.WRITE_CALENDAR)

class EventDetailFragment : BaseFragment<FragmentEventDetailBinding>() {

    private val eventDetailViewModel: EventDetailViewModel by viewModel()
    private var currentEvent: EventBo? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(getString(R.string.navigation_event__uuid__argument))?.let {
            setupEventDetailObserver(it)
        }
        setupClickListeners()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermissionUtil.isPermissionGranted(requestCode, grantResults)) {
            saveEvent()
        }
    }

    private fun setupClickListeners() {
        viewBinding.eventDetailImgBack.setOnClickListener {
            popBack()
        }
        viewBinding.eventDetailImgAddToCalendar.setOnClickListener {
            clickOnAddToCalendar()
        }
        viewBinding.eventDetailBtnGoToLocation.setOnClickListener {
            currentEvent?.location?.location?.toLatLng()?.let { latlng ->
                context?.startGoogleMapsByLatLng(
                    latlng
                )
            }
        }
    }

    private fun clickOnAddToCalendar() {
        context?.let { context ->
            if (PermissionUtil.needPermission(context, *PERMISSIONS)) {
                PermissionUtil.requestPermission(this, *PERMISSIONS)
            } else {
                saveEvent()
            }
        }
    }

    private fun saveEvent() {
        context?.startCalendarEvent(
            currentEvent?.title,
            currentEvent?.date?.time
        )
    }

    private fun setupEventDetailObserver(uuid: String) {
        eventDetailViewModel.getEventDetailData()
            .observe(viewLifecycleOwner, object : ResourceObserver<EventBo>() {
                override fun onSuccess(response: EventBo?) {
                    response?.let {
                        currentEvent = it
                        setupView(it)
                    }
                }

            })
        eventDetailViewModel.getEventDetail(uuid)

    }

    private fun setupView(event: EventBo) {
        viewBinding.apply {
            eventDetailLabelTitle.text = event.title
            event.date?.let {
                eventDetailLabelDate.text = DateUtil.format(it)
            }
            eventDetailLabelType.text = event.eventType
            eventDetailLabelBody.text = event.description
        }
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