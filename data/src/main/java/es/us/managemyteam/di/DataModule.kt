package es.us.managemyteam.di

import es.us.managemyteam.repository.ClubRepository
import es.us.managemyteam.repository.ClubRepositoryImpl
import es.us.managemyteam.repository.EventRepository
import es.us.managemyteam.repository.EventRepositoryImpl
import org.koin.dsl.module

val dataModule = module {

    single<EventRepository> { EventRepositoryImpl() }
    single<ClubRepository> { ClubRepositoryImpl() }

}