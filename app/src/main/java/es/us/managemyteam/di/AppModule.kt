package es.us.managemyteam.di

import es.us.managemyteam.contract.PaypalInterface
import es.us.managemyteam.manager.PaypalManager
import es.us.managemyteam.ui.viewmodel.*
import es.us.managemyteam.usecase.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // General

    single<PaypalInterface> {
        PaypalManager().apply { initialize(get()) }
    }

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

    factory {
        GetEventDetailUc(get())
    }

    factory {
        GetCurrentCallUc(get())
    }

    factory {
        SetCurrentCallUc(get())
    }

    viewModel {
        EventsViewModel(get(), get())
    }

    viewModel {
        CreateEventViewModel(get(), get(), get(), get(), get(), get(), get(), get())
    }

    viewModel {
        ChatViewModel(get(), get(), get())
    }

    viewModel {
        MyTeamViewModel(get(), get())
    }

    viewModel {
        EventDetailViewModel(get(), get())
    }

    viewModel {
        PastEventsViewModel(get(), get())
    }

    // Club

    factory {
        GetClubUc(get())
    }

    factory {
        EditClubUc(get())
    }

    viewModel {
        ClubViewModel(get(), get())
    }

    viewModel {
        EditClubViewModel(get(), get(), get())
    }

    //Payment

    factory {
        CreatePaymentUc(get())
    }

    factory {
        GetMyPaymentsUc(get())
    }

    factory {
        GetPaypalConfigUc(get())
    }

    factory {
        CreatePaypalConfigUc(get())
    }

    viewModel {
        AdminPaypalViewModel(get(), get())
    }

    viewModel {
        MyPaymentsViewModel(get())
    }

    viewModel {
        CreatePaymentViewModel(get(), get())
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

    factory {
        UpdateUserProfileUc(get())
    }

    factory {
        UpdateEmailUc(get())
    }

    factory {
        UpdatePasswordUc(get())
    }

    factory {
        RecoverPasswordUc(get())
    }

    factory {
        SetCurrentNewUserUc(get())
    }

    factory {
        GetCurrentNewUserUc(get())
    }

    viewModel {
        RegistrationViewModel(get(), get(), get())
    }

    viewModel {
        LoginViewModel(get(), get())
    }

    viewModel {
        UserProfileViewModel(get(), get())
    }

    viewModel {
        UpdateUserProfileViewModel(get(), get())
    }

    viewModel {
        UpdateEmailViewModel(get(), get())
    }

    viewModel {
        UpdatePasswordViewModel(get(), get(), get())
    }

    viewModel {
        RecoverPasswordViewModel(get())
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

    viewModel {
        CreateCoachViewModel(get())
    }

    // Call

    factory {
        GetCallsByUserIdUC(get())
    }

    factory {
        AcceptCallUc(get())
    }

    factory {
        RejectCallUc(get())
    }

    viewModel {
        PendingCallViewModel(get(), get(), get())
    }

    viewModel {
        AcceptedCallViewModel(get(), get())
    }

    viewModel {
        RejectedCallViewModel(get(), get())
    }

}