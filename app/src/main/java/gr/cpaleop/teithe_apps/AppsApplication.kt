package gr.cpaleop.teithe_apps

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.FirebaseCrashlytics
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.download.di.downloadModule
import gr.cpaleop.teithe_apps.di.coreModule
import gr.cpaleop.teithe_apps.di.networkModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class AppsApplication : Application() {

    private val preferencesRepository: PreferencesRepository by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppsApplication)
            modules(networkModule, coreModule, downloadModule)
        }

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(BuildConfig.DEBUG)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        runBlocking {
            val preferredNightMode = preferencesRepository.getNightModeFlow().first()
            if (preferredNightMode != AppCompatDelegate.MODE_NIGHT_YES && preferredNightMode != AppCompatDelegate.MODE_NIGHT_NO) {
                preferencesRepository.updateNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else {
                AppCompatDelegate.setDefaultNightMode(preferredNightMode)
            }
        }
    }
}