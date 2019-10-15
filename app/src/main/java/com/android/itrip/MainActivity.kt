package com.android.itrip


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.android.itrip.databinding.ActivityMainBinding
import com.android.itrip.services.ApiService
import com.android.itrip.services.QuizService
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {

    private val logger = Logger.getLogger(this::class.java.name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val currentUser =
            intent.getParcelableExtra<com.google.firebase.auth.FirebaseUser>("CurrentUser")

        ApiService.setContext(this)

        Picasso.get().load(currentUser.photoUrl).placeholder(R.drawable.ic_person_primary_24dp)
            .error(R.drawable.ic_person_primary_24dp).into(binding.imageView)

        binding.logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        QuizService.getQuestionsVerify({ answered: Boolean -> quizAnswered(answered) }, {})
        binding.currentUser = currentUser
    }

    private fun quizAnswered(answered: Boolean) {
        val navHostFragment = navHostFragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation)
        if (!answered) {
            graph.startDestination = R.id.quizInfoFragment
            navHostFragment.navController.graph = graph
        } else {
            graph.startDestination = R.id.homeFragment
            navHostFragment.navController.graph = graph
        }
    }

}
