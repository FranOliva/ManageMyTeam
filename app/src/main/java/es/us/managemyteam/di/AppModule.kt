package es.us.managemyteam.di

import es.us.managemyteam.ui.viewmodel.CreateEventViewModel
import es.us.managemyteam.ui.viewmodel.EventsViewModel
import es.us.managemyteam.ui.viewmodel.RegistrationViewModel
import es.us.managemyteam.usecase.*
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

    factory {
        CreateEventUc(get())
    }

    factory {
        GetCurrentNewEventUc(get())
    }

    factory {
        SetCurrentNewEventUc(get())
    }


    viewModel {
        EventsViewModel(get())
    }

    viewModel {
        CreateEventViewModel(get(), get(), get(), get(), get())
    }

    // User

    factory {
        PostRegistrationUc(get())
    }

    viewModel {
        RegistrationViewModel(get())
    }
}