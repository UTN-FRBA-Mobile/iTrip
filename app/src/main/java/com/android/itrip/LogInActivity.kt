package com.android.itrip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.itrip.services.ApiService
import com.android.itrip.services.AuthenticationService
import com.android.itrip.util.NukeSSLCerts
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


class LogInActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private var providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        NukeSSLCerts().nuke()
        ApiService.setContext(this)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            AuthenticationService.verifyUser(
                FirebaseAuth.getInstance().currentUser,
                { userVerifiedCallback() },
                {})
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
                    {})
            } else {
                Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun userVerifiedCallback() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("CurrentUser", FirebaseAuth.getInstance().currentUser)
        }
        startActivity(intent)
        finish()
        Toast.makeText(
            this,
            "Welcome! " + FirebaseAuth.getInstance().currentUser?.displayName!!,
            Toast.LENGTH_LONG
        ).show()
    }
}
