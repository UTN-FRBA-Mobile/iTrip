package com.android.itrip


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.itrip.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val currentUser =
            intent.getParcelableExtra<com.google.firebase.auth.FirebaseUser>("CurrentUser")

        binding.currentUser = currentUser
    }

}
