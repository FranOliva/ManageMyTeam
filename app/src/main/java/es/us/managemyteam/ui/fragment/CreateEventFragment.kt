package es.us.managemyteam.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.data.model.LocationBo
import es.us.managemyteam.databinding.FragmentCreateEventBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.view.common_map.MapListener
import es.us.managemyteam.ui.view.common_map.MarkerItemVo
import es.us.managemyteam.ui.viewmodel.CreateEventViewModel
import es.us.managemyteam.util.DateUtil
import es.us.managemyteam.util.EventUtil
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateEventFragment : BaseFragment<FragmentCreateEventBinding>(), MapListener {

    private var selectedDate = Calendar.getInstance()
    private val createEventViewModel: CreateEventViewModel by viewModel()
    private var locationSelected: LocationBo? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMap(savedInstanceState)
        setupClickListeners()
        setupObservers()
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        viewBinding.createEventViewMap.apply {
            onCreate(savedInstanceState)
            setEnableInteraction(false)
            setShowUserPosition(false)
            showLocationButton(false)
            setMapListener(this@CreateEventFragment)
        }
    }

    private fun setupObservers() {
        setupCreateEventObserver()
        setupGetCurrentNewEventObserver()
        setupLocationSelectedObserver()
    }

    private fun setupClickListeners() {
        setupDateClickListener()
        setupEventTypeClickListener()
        viewBinding.createEventBtnSelectPlayers.setOnClickListener {
            findNavController().navigate(R.id.action_create_event_to_select_players)
        }
        viewBinding.createEventBtnSave.setOnClickListener {
            clickOnSave()
        }
        setupLocationClickListener()
    }

    private fun setupViews() {
        if (locationSelected == null) {
            viewBinding.createEventContainerMap.visibility = GONE
            viewBinding.createEventLabelLocation.visibility = VISIBLE
        } else {
            viewBinding.createEventLabelLocation.visibility = GONE
            viewBinding.createEventContainerMap.visibility = VISIBLE
        }
    }

    //region Map

    override fun onResume() {
        super.onResume()
        viewBinding.createEventViewMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewBinding.createEventViewMap.onPause()
    }

    override fun onStart() {
        super.onStart()
        viewBinding.createEventViewMap.onStart()
    }

    override fun onStop() {
        super.onStop()
        viewBinding.createEventViewMap.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        viewBinding.createEventViewMap.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.createEventViewMap.onDestroy()
        createEventViewModel.clearEvent()
        createEventViewModel.setLocationSelected(null)
    }

    //endregion

    //region Observers
    private fun setupCreateEventObserver() {
        createEventViewModel.createEventData()
            .observe(viewLifecycleOwner, object : ResourceObserver<Boolean>() {
                override fun onSuccess(response: Boolean?) {
                    response?.let {
                        createEventViewModel.clearEvent()
                        Toast.makeText(
                            context,
                            getString(R.string.create_event_success),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        popBack()
                    }
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(
                        getString(error.errorMessageId)
                    )
                }

            })
    }

    private fun setupLocationSelectedObserver() {
        createEventViewModel.getLocationSelectedData()
            .observe(viewLifecycleOwner, object : ResourceObserver<LocationBo?>() {
                override fun onSuccess(response: LocationBo?) {
                    locationSelected = response
                    setupViews()
                    response?.let {
                        setupMinimap(it)
                    }
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(getString(error.errorMessageId))
                }
            })
        createEventViewModel.getLocationSelected()
    }

    private fun setupGetCurrentNewEventObserver() {
        createEventViewModel.getCurrentNewEventData()
            .observe(viewLifecycleOwner, object : ResourceObserver<EventBo>() {
                override fun onSuccess(response: EventBo?) {
                    response?.let {
                        viewBinding.createEventEditTextTitle.setText(it.title)
                        if (it.date != null) {
                            viewBinding.createEventEditTextDate.setText(DateUtil.format(it.date!!))
                        }
                        viewBinding.createEventEditTextDescription.setText(it.description)
                        viewBinding.createEventEditTextEventType.setText(it.eventType)
                    }
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(
                        getString(error.errorMessageId),
                        getDefaultDialogErrorListener()
                    )
                }
            })
        createEventViewModel.getCurrentNewEvent()
    }

    private fun setCurrentEvent() {
        val title = viewBinding.createEventEditTextTitle.text.trim()
        val sdf = SimpleDateFormat(getString(R.string.date_time_format), Locale.getDefault())
        val date = if (!viewBinding.createEventEditTextDate.text.isBlank()) {
            sdf.parse(viewBinding.createEventEditTextDate.text)
        } else {
            null
        }
        val description = viewBinding.createEventEditTextDescription.text.trim()
        val eventType = viewBinding.createEventEditTextEventType.text.trim()
        val call = null

        createEventViewModel.setCurrentNewEvent(
            EventBo(
                title, date, locationSelected, description, call, eventType
            )
        )
    }

    //endregion

    //region Clicks

    private fun clickOnSave() {
        val title = viewBinding.createEventEditTextTitle.text.trim()
        val sdf = SimpleDateFormat(getString(R.string.date_time_format), Locale.getDefault())
        val date = if (!viewBinding.createEventEditTextDate.text.isBlank()) {
            sdf.parse(viewBinding.createEventEditTextDate.text)
        } else {
            null
        }
        val description = viewBinding.createEventEditTextDescription.text.trim()
        val eventType = viewBinding.createEventEditTextEventType.text.trim()
        val assistants = null

        getFocusedView().hideKeyboard()

        createEventViewModel.createEvent(
            title,
            date,
            eventType,
            description,
            locationSelected,
            assistants
        )
    }

    private fun setupLocationClickListener() {
        viewBinding.createEventContainerLocation.setOnClickListener {
            openSelectLocationFragment()
        }
    }

    private fun openSelectLocationFragment() {
        setCurrentEvent()
        findNavController().navigate(R.id.action_create_event_to_select_location)
    }

    private fun setupEventTypeClickListener() {
        viewBinding.createEventEditTextEventType.clickListener {
            context?.let { context ->
                val dialog = AlertDialog.Builder(context, R.style.DialogTheme)
                val list = EventUtil.getEventTypesList(context)
                dialog.apply {
                    setTitle(getString(R.string.create_event_event_type))
                    setSingleChoiceItems(
                        list, -1
                    ) { _, which ->
                        viewBinding.createEventEditTextEventType.setText(list[which])
                    }
                    setCancelable(false)
                    setPositiveButton(R.string.accept) { dialog, _ ->
                        dialog.cancel()
                    }
                    setNegativeButton(
                        R.string.cancel
                    ) { dialog, _ -> dialog.cancel() }
                    show()
                }
            }

        }
    }

    private fun setupDateClickListener() {

        viewBinding.createEventEditTextDate.clickListener {
            showDateDialog(
                selectedDate,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    updateSelectedDate(dayOfMonth, month + 1, year)
                    showTimePickerDialog(dayOfMonth, month + 1, year)
                })
        }
    }

    //endregion

    //region Dialogs

    private fun updateSelectedDate(day: Int, month: Int, year: Int) {
        selectedDate = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
        }
    }

    private fun showDateDialog(
        date: Calendar,
        dateListener: DatePickerDialog.OnDateSetListener
    ) {
        val year = date[Calendar.YEAR]
        val month = date[Calendar.MONTH]
        val day = date[Calendar.DAY_OF_MONTH]

        context?.let {
            DatePickerDialog(
                it,
                R.style.DialogTheme,
                dateListener,
                year,
                month,
                day
            ).apply {
                datePicker.minDate = Date().time
                show()
            }
        }
    }

    private fun showTimePickerDialog(day: Int, month: Int, year: Int) {
        context?.let {
            TimePickerDialog(
                it,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    viewBinding.createEventEditTextDate.setText(
                        String.format(
                            getString(R.string.create_event_date_format),
                            day,
                            month,
                            year,
                            hourOfDay,
                            minute
                        )
                    )
                }, 0, 0, true
            ).apply {
                setCancelable(false)
                show()
            }
        }
    }

    //endregion

    private fun setupMinimap(location: LocationBo) {
        val mapView = viewBinding.createEventViewMap
        val marker = location.location?.getMarkerItemVo(mapView)
        marker?.let {
            mapView.addMarker(it)
            viewBinding.createEventLabelAddress.text =
                mapView.getAddressFromMarkerPosition(it.position)
        }

    }

    //region BaseFragment

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateEventBinding {
        return FragmentCreateEventBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.create_event_title))
            setNavIcon(ContextCompat.getDrawable(context, R.drawable.ic_back))
            setNavAction { popBack() }
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

    override fun onMapClicked(latLng: LatLng) {
        openSelectLocationFragment()
    }

    override fun onMarkerClicked(markerClicked: MarkerItemVo) {
        openSelectLocationFragment()
    }

    override fun onMapReady() {
        viewBinding.createEventViewMap.apply {
            initialize(this@CreateEventFragment)
        }
    }

    override fun onLocationChanged(latLng: LatLng) {
        // no-op
    }

    //endregion

}