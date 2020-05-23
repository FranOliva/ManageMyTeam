package es.us.managemyteam.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.database.DatabaseTables
import es.us.managemyteam.databinding.FragmentCreateEventBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.util.EventUtil
import java.util.*


class EventCreateFragment : BaseFragment<FragmentCreateEventBinding>() {

    val eventsRef = getDatabase().getReference(DatabaseTables.EVENT_TABLE)
    private var selectedDate = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun saveEvent(event: EventBo) {

    }

    private fun setupClickListeners() {
        setupDateClickListener()
        setupEventTypeClickListener()
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
                    updateSelectedDate(dayOfMonth, month, year)
                    showTimePickerDialog(dayOfMonth, month, year)
                })
        }
    }

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
                        resources.getString(
                            R.string.create_event_date_placeholder,
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

    //endregion

}