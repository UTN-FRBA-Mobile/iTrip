package com.android.itrip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.itrip.services.AuthenticationService
import com.android.itrip.util.NukeSSLCerts
import com.android.itrip.util.VolleyController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.logging.Logger


class LogInActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private var providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
//        AuthUI.IdpConfig.PhoneBuilder().build(),
//        AuthUI.IdpConfig.FacebookBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var queue: VolleyController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        NukeSSLCerts().nuke()
        queue = VolleyController.getInstance(this)

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            AuthenticationService.verifyUser(auth.currentUser, queue)
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("CurrentUser", auth.currentUser)
            }
            startActivity(intent)
            finish()
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
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                AuthenticationService.verifyUser(user, queue)
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("CurrentUser", user)
                }
                startActivity(intent)
                finish() // Call once you redirect to another activity
                Toast.makeText(this, "Welcome! " + user?.displayName!!, Toast.LENGTH_LONG).show()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_LONG).show()
            }
        }
    }

//    https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md
}
