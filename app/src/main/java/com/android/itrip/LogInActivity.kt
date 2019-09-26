package com.android.itrip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.itrip.util.NukeSSLCerts
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import java.util.logging.Logger


class LogInActivity : AppCompatActivity() {
    // Choose an arbitrary request code value
    private val RC_SIGN_IN = 123
    private var providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
//        AuthUI.IdpConfig.PhoneBuilder().build(),
//        AuthUI.IdpConfig.FacebookBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )
    val logger = Logger.getLogger(LogInActivity::class.java.name)

    fun getApiToken(auth: FirebaseAuth?) {
        val queue = Volley.newRequestQueue(this)
        val base_api_url = resources.getString(R.string.base_api_url)
        val url = base_api_url + "token/"
        var accessToken = ""
        var refreshToken = ""
        val updateJsonobj = JSONObject()
        updateJsonobj.put("username", "admin")
        updateJsonobj.put("password", "SlaidTeam123")

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, updateJsonobj,
            Response.Listener { response ->
                logger.info("Response: %s".format(response.toString()))
                refreshToken = response.getString("refresh")
                accessToken = response.getString("access")
                auth?.signInWithCustomToken(accessToken)
            },
            Response.ErrorListener { response -> logger.info("That didn't work! " + response.message) })

        queue.add(jsonObjectRequest)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        NukeSSLCerts().nuke()
        val auth = FirebaseAuth.getInstance()
        getApiToken(auth)
        if (auth.currentUser != null) {
            // already signed in
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("CurrentUser", auth.currentUser)
            }
            startActivity(intent)
            finish() // Call once you redirect to another activity
        } else {
            // not signed in
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
