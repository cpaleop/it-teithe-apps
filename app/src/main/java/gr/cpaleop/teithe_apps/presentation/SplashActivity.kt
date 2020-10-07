package gr.cpaleop.teithe_apps.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.teithe_apps.databinding.ActivitySplashBinding
import gr.cpaleop.teithe_apps.di.splashModule
import gr.cpaleop.teithe_apps.presentation.base.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import java.util.*

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val preferencesRepository: PreferencesRepository by inject()

    override fun inflateViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(splashModule)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        unloadKoinModules(splashModule)
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    /*override fun attachBaseContext(newBase: Context) {
        var language = preferencesRepository.getString(PreferencesRepository.LANGUAGE) ?: return
        if (language == "") {
            language = Locale.getDefault().language
            preferencesRepository.putString(PreferencesRepository.LANGUAGE, language)
        }
        val context = setLocale(newBase, language)
        super.attachBaseContext(context)
    }*/

    private fun setLocale(newBase: Context, language: String): Context? {
        val locale =
            if (LanguageCode.GREEK == language) Locale(LanguageCode.GREEK) else Locale.ENGLISH
        try {
            Locale.setDefault(locale)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        return updateResources(newBase, locale)
    }

    private fun updateResources(
        context: Context,
        locale: Locale
    ): Context? {
        val resources = context.resources
        val configuration =
            Configuration(resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }
}