package gr.cpaleop.teithe_apps.presentation.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private val preferencesRepository: PreferencesRepository by inject()
    protected lateinit var binding: VB

    abstract fun inflateViewBinding(): VB

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateViewBinding()
        setContentView(binding.root)
    }

    override fun attachBaseContext(newBase: Context) {
        lifecycleScope.launch {
            var language = preferencesRepository.getLanguageFlow().first()
            if (language == "") {
                language = LanguageCode.ENGLISH
                preferencesRepository.updateLanguage(language)
            }
            val context = setLocale(newBase, language)
            super.attachBaseContext(context)
        }
    }

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