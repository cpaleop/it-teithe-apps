package gr.cpaleop.teithe_apps

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.teithe_apps.di.coreModule
import gr.cpaleop.teithe_apps.di.networkModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class AppsApplication : Application() {

    private val preferencesRepository: PreferencesRepository by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppsApplication)
            modules(networkModule, coreModule)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        AppCompatDelegate.setDefaultNightMode(preferencesRepository.getInt(PreferencesRepository.NIGHT_MODE))
    }
}