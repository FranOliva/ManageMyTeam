package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.ViewModel
import es.us.managemyteam.usecase.GetEventLocationUc

class EventsViewModel(
    private val getEventLocationUc: GetEventLocationUc,
    private val setEventLocationUc: GetEventLocationUc
) : ViewModel() {
}