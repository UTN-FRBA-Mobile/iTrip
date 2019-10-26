package com.android.itrip

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.android.itrip.databinding.ActivityQuizBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_quiz.*

interface DrawerLocker {
    fun setDrawerEnabled(enabled: Boolean)
}

class QuizActivity : AppCompatActivity(), OnNavigationItemSelectedListener, DrawerLocker {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings()
        initDrawer()
        initNavigation()
        startQuiz()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    private fun bindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)
        drawerLayout = binding.drawerLayoutQuiz
        toolbar = binding.appBarQuiz as Toolbar
        navigationView = binding.navigationViewQuiz
    }

    private fun initDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.navhostfragment_quiz),
            drawerLayout
        )
    }

    private fun initNavigation() {
        // settea el toolbar y el usuario logueado en el menu
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        // settea navigation controller y bindings del navigation view
        navController = Navigation.findNavController(this, R.id.navhostfragment_quiz)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode = if (enabled) LOCK_MODE_UNLOCKED else LOCK_MODE_LOCKED_CLOSED
        drawerLayout.setDrawerLockMode(lockMode)
        drawerToggle.isDrawerIndicatorEnabled = enabled
    }

    private fun startQuiz() {
        val navHostFragment = navhostfragment_quiz as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.quiz_navigation)
        graph.startDestination = R.id.quizInfoFragment
        navHostFragment.navController.graph = graph
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("not implemented")
    }

}