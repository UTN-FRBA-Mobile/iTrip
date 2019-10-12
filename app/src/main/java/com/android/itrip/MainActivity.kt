package com.android.itrip


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.itrip.databinding.ActivityMainBinding
import com.android.itrip.services.TravelService
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {

    private val logger = Logger.getLogger(this::class.java.name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val currentUser =
            intent.getParcelableExtra<com.google.firebase.auth.FirebaseUser>("CurrentUser")

        TravelService.setContext(this)

        currentUser?.providerData?.forEach {
            logger.info("Sign-in provider: " + it.providerId)
            logger.info("  Provider-specific UID: " + it.uid)
            logger.info("  Name: " + it.displayName)
            logger.info("  Email: " + it.email)
            logger.info("  Photo URL: " + it.photoUrl)
        }

        if (currentUser != null) {
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

        binding.currentUser = currentUser
    }


}
