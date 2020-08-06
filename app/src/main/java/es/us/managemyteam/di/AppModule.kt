package es.us.managemyteam.di

import es.us.managemyteam.ui.viewmodel.*
import es.us.managemyteam.ui.viewmodel.ClubViewModel
import es.us.managemyteam.ui.viewmodel.CreateEventViewModel
import es.us.managemyteam.ui.viewmodel.EventsViewModel
import es.us.managemyteam.usecase.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // General

    viewModel {
        MenuViewModel()
    }

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

    // Club

    factory {
        GetClubUc(get())
    }

    viewModel {
        ClubViewModel(get())
    }

    // User

    factory {
        PostRegistrationUc(get())
    }

    factory {
        LoginUc(get())
    }

    factory {
        GetUserUc(get())
    }

    viewModel {
        RegistrationViewModel(get())
    }

    viewModel {
        LoginViewModel(get(), get())
    }
}