package gr.cpaleop.authentication.presentation

import android.content.Intent
import android.os.Bundle
import gr.cpaleop.authentication.databinding.ActivityAuthenticationBinding
import gr.cpaleop.authentication.di.authenticationModule
import gr.cpaleop.core.presentation.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class AuthenticationActivity : BaseActivity<ActivityAuthenticationBinding>() {

    private val viewModel: AuthenticationViewModel by viewModel()

    override fun inflateViewBinding(): ActivityAuthenticationBinding {
        return ActivityAuthenticationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(authenticationModule)
        super.onCreate(savedInstanceState)
        viewModel.presentUri()
    }

    override fun onDestroy() {
        unloadKoinModules(authenticationModule)
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent?.action
        if (Intent.ACTION_VIEW == action && intent.data != null) {
            val code = intent.data?.getQueryParameter("code")
            viewModel.retrieveToken(code)
        }
    }
}