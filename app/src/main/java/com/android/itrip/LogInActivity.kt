package com.android.itrip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.itrip.databinding.ActivityLoginBinding
import com.android.itrip.services.ApiError
import com.android.itrip.services.ApiService
import com.android.itrip.services.AuthenticationService
import com.android.itrip.util.NukeSSLCerts
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.logging.Logger


class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val logger = Logger.getLogger(this::class.java.name)
    private val RC_SIGN_IN = 123
    private var providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        NukeSSLCerts().nuke()
        ApiService.setContext(this)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            AuthenticationService.verifyUser(
                FirebaseAuth.getInstance().currentUser,
                { userVerifiedCallback() },
                { error -> handleError(error) })
        } else {
            showSignInOptions()
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme)
                .setIsSmartLockEnabled(false, true)
                .setLogo(R.drawable.logo)
                .build(), RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                AuthenticationService.verifyUser(
                    FirebaseAuth.getInstance().currentUser,
                    { userVerifiedCallback() },
                    { error -> handleError(error) })
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finish() // force to finish in this activity
            } else {
                when {
                    response == null -> finish() // force to finish in this activity
                    response.error?.errorCode == ErrorCodes.NO_NETWORK -> Toast.makeText(
                        this,
                        "No hay conexión a Internet",
                        Toast.LENGTH_LONG
                    ).show()
                    else -> Toast.makeText(this, response.error?.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleError(error: ApiError) {
        binding.linearlayoutNoInternetConnection.visibility = View.VISIBLE
        binding.imageviewRefresh.setOnClickListener {
            // refresh activity until recover internet
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        logger.severe(
            "Failed to verify user with Firebase " +
                    "- statusCode: ${error.statusCode} " +
                    "- message: ${error.message} " +
                    "- data: ${error.data}"
        )
    }

    private fun userVerifiedCallback() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("CurrentUser", FirebaseAuth.getInstance().currentUser)
        }
        startActivity(intent)
        finish()
    }

}
