package com.android.itrip


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.itrip.databinding.ActivityMainBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.logging.Logger
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.android.itrip.util.NukeSSLCerts


class MainActivity : AppCompatActivity() {

    val logger = Logger.getLogger(MainActivity::class.java.name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val currentUser =
            intent.getParcelableExtra<com.google.firebase.auth.FirebaseUser>("CurrentUser")


        currentUser?.providerData?.forEach {
            logger.info("Sign-in provider: " + it.providerId)
            logger.info("  Provider-specific UID: " + it.uid)
            logger.info("  Name: " + it.displayName)
            logger.info("  Email: " + it.email)
            logger.info("  Photo URL: " + it.photoUrl)
        }

        if (currentUser != null) {
            logger.info("currentUser idToken: " + currentUser.getIdToken(true))
            logger.info("currentUser idToken: " + currentUser.getIdToken(false))
            logger.info("currentUser displayName: " + currentUser.displayName)
            logger.info("currentUser uid: " + currentUser.uid)
            logger.info("currentUser email: " + currentUser.email)
            logger.info("currentUser isEmailVerified: " + currentUser.isEmailVerified)
            logger.info("currentUser photoUrl: " + currentUser.photoUrl)
        }


        Picasso.get().load(currentUser.photoUrl).into(binding.imageView)

        binding.logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
//        binding.logOutButton.setOnClickListener {
//            getApiToken(null)
//        }

        binding.currentUser = currentUser
    }

//    fun getApiToken(auth: FirebaseAuth?) {
//        val queue = Volley.newRequestQueue(this)
//        val base_api_url = resources.getString(R.string.base_api_url)
//        val url = base_api_url + "token/"
//
//        val updateJsonobj = JSONObject()
//        updateJsonobj.put("username", "admin")
//        updateJsonobj.put("password", "SlaidTeam123")
//
//        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, updateJsonobj,
//            Response.Listener { response ->
//                logger.info("Response: %s".format(response.toString()))
//            },
//            Response.ErrorListener { response -> logger.info("That didn't work! " + response.message) })
//
//        queue.add(jsonObjectRequest)
//    }

}
