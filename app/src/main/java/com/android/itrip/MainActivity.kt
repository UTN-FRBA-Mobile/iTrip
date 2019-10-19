package com.android.itrip


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.android.itrip.databinding.ActivityMainBinding
import com.android.itrip.databinding.AppBarHeaderBinding
import com.android.itrip.services.ApiService
import com.android.itrip.services.QuizService
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiService.setContext(this)
        bindings()
        initDrawer()
        initNavigation()
        QuizService.getQuestionsVerify({ answered: Boolean -> quizAnswered(answered) }, {})
    }

    private fun bindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        toolbar = binding.appBar as Toolbar
        navigationView = binding.navigationView
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

    private fun initNavigation() {
        // settea el toolbar y el usuario logueado en el menu
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val bindingAppBar: AppBarHeaderBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.app_bar_header, navigationView, false
        )
        val currentUser = intent.getParcelableExtra<FirebaseUser>("CurrentUser")
        bindingAppBar.currentUser = currentUser
        // settea navigation controller y bindings del navigation view
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)
        navigationView.addHeaderView(bindingAppBar.root)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.navHostFragment),
            drawerLayout
        )
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.drawer_menu_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            }
            R.id.drawer_menu_create_travel -> navController.navigate(R.id.createTravelFragment)
            R.id.drawer_menu_travels -> navController.navigate(R.id.homeFragment)
        }
        return true
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
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
