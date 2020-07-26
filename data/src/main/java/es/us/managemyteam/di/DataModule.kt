package es.us.managemyteam.di

import es.us.managemyteam.repository.EventRepository
import es.us.managemyteam.repository.EventRepositoryImpl
import es.us.managemyteam.repository.UserRepository
import es.us.managemyteam.repository.UserRepositoryImpl
import org.koin.dsl.module

val dataModule = module {

    single<EventRepository> { EventRepositoryImpl() }

    single<UserRepository> { UserRepositoryImpl() }

}