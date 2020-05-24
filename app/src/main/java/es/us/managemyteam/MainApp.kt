package es.us.managemyteam

import android.app.Application
import es.us.managemyteam.di.appModule
import es.us.managemyteam.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(this@MainApp)
            modules(
                listOf(
                    appModule,
                    dataModule
                )
            )
        }
    }
}