package es.us.managemyteam.di

import es.us.managemyteam.ui.viewmodel.EventsViewModel
import es.us.managemyteam.usecase.GetEventLocationUc
import es.us.managemyteam.usecase.GetEventsUc
import es.us.managemyteam.usecase.SetEventLocationUc
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Eventos

    factory {
        GetEventLocationUc(get())
    }

    factory {
        SetEventLocationUc(get())
    }

    factory {
        GetEventsUc(get())
    }

    viewModel {
        EventsViewModel(get(), get(), get())
    }

}