package es.us.managemyteam.di

import es.us.managemyteam.repository.*
import org.koin.dsl.module

val dataModule = module {

    single<EventRepository> { EventRepositoryImpl() }

    single<ClubRepository> { ClubRepositoryImpl() }

    single<UserRepository> { UserRepositoryImpl() }

    single<AdminRepository> { AdminRepositoryImpl() }

    single<ChatRepository> { ChatRepositoryImpl() }

    single<CallRepository> { CallRepositoryImpl() }

}