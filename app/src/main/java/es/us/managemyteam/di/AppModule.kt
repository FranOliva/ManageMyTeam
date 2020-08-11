package es.us.managemyteam.di

import es.us.managemyteam.ui.viewmodel.*
import es.us.managemyteam.usecase.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // General

    viewModel {
        MenuViewModel(get(), get())
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
        EventsViewModel(get(), get())
    }

    viewModel {
        CreateEventViewModel(get(), get(), get(), get(), get())
    }

    viewModel {
        ClubViewModel(get())
    }

    viewModel {
        ChatViewModel(get(), get(), get())
    }

    // Club

    factory {
        GetClubUc(get())
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

    factory {
        LogoutUc(get())
    }

    factory {
        RemoveUserUc(get())
    }

    viewModel {
        RegistrationViewModel(get())
    }

    viewModel {
        LoginViewModel(get(), get())
    }

    // Chat

    factory {
        GetMessagesUc(get())
    }

    factory {
        PostMessageUc(get())
    }

    // Administration

    factory {
        GetPlayersUc(get())
    }

    factory {
        AcceptPlayerUc(get())
    }

    viewModel {
        AcceptPlayersViewModel(get(), get(), get())
    }
}