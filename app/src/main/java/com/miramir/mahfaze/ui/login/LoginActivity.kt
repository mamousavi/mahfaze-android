package com.miramir.mahfaze.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.miramir.mahfaze.R
import com.miramir.mahfaze.data.model.Resource
import com.miramir.mahfaze.data.worker.SyncWorker
import com.miramir.mahfaze.ui.base.BaseActivity
import com.miramir.mahfaze.ui.main.MainActivity
import com.miramir.mahfaze.util.PreferencesManager
import com.miramir.mahfaze.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginActivity : BaseActivity() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
        viewModel.result.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS ->
                    if (!it.isHandled) {
                        it.isHandled = true

                        progress.visibility = View.GONE

                        PreferencesManager.putUserId(applicationContext, it.data!!.id)
                        PreferencesManager.putUserEmail(applicationContext, it.data.email)
                        PreferencesManager.putUserPassword(applicationContext, viewModel.getCredentials()!!.password)

                        val syncWork = PeriodicWorkRequestBuilder<SyncWorker>(3600, TimeUnit.SECONDS)
                                .setConstraints(Constraints.Builder()
                                        .setRequiredNetworkType(NetworkType.CONNECTED)
                                        .build())
                                .build()
                        WorkManager.getInstance().enqueue(syncWork)

                        startActivity(Intent(this, MainActivity::class.java))

                        finish()
                    }
                Resource.Status.ERROR ->
                    if (!it.isHandled) {
                        it.isHandled = true
                        progress.visibility = View.GONE
                        when (it.error) {
                            is HttpException -> Snackbar.make(login, R.string.error_network, Snackbar.LENGTH_LONG).show()
                            else -> Snackbar.make(login, R.string.error_connection, Snackbar.LENGTH_LONG).show()
                        }
                    }
                Resource.Status.LOADING -> progress.visibility = View.VISIBLE
            }
        })

        login.setOnClickListener { _ ->
            val emailText = email.editText!!.text.toString()
            val passwordText = password.editText!!.text.toString()

            val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
            val isValidPassword = passwordText.length >= 8
            val isAllValid = isValidEmail && isValidPassword

            email.error = null
            password.error = null

            if (!isValidEmail) {
                email.error = getString(R.string.error_email)
            }
            if (!isValidPassword) {
                password.error = getString(R.string.error_password)
            }
            if (!isAllValid) {
                return@setOnClickListener
            }

            currentFocus?.let { hideSoftKeyboard(it) }

            viewModel.login(emailText, passwordText)
        }
    }
}