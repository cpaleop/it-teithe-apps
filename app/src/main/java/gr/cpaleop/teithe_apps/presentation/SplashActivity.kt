package gr.cpaleop.teithe_apps.presentation

import android.os.Bundle
import gr.cpaleop.core.presentation.base.BaseActivity
import gr.cpaleop.teithe_apps.databinding.ActivitySplashBinding
import gr.cpaleop.teithe_apps.di.splashModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

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
}