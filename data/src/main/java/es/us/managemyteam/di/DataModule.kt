package es.us.managemyteam.di

import es.us.managemyteam.constant.Notification
import es.us.managemyteam.contract.PreferencesInterface
import es.us.managemyteam.data.ws.NotificationWs
import es.us.managemyteam.datasource.NotificationApiDataSource
import es.us.managemyteam.datasource.NotificationApiDataSourceImpl
import es.us.managemyteam.manager.PreferencesManager
import es.us.managemyteam.repository.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single {
        val interceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(Notification.FIREBASE_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { PreferencesManager(get()) as PreferencesInterface }

    single<EventRepository> { EventRepositoryImpl() }

    single<ClubRepository> { ClubRepositoryImpl() }

    single<UserRepository> { UserRepositoryImpl(get()) }

    single<AdminRepository> { AdminRepositoryImpl() }

    single<ChatRepository> { ChatRepositoryImpl() }

    single<PaymentRepository> { PaymentRepositoryImpl() }

    single<CallRepository> { CallRepositoryImpl() }

    single<NotificationRepository> {
        NotificationRepositoryImpl(get())
    }

    factory {
        get<Retrofit>().create(NotificationWs::class.java)
    }

    factory {
        NotificationApiDataSourceImpl(get()) as NotificationApiDataSource
    }

}