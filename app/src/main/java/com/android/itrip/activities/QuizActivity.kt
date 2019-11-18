package com.android.itrip.activities

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
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.android.itrip.R
import com.android.itrip.databinding.ActivityQuizBinding
import com.android.itrip.databinding.AppBarHeaderBinding
import com.android.itrip.util.CircleTransformation
import com.android.itrip.util.DrawerLocker
import com.android.itrip.viewModels.QuizViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.app_bar.view.*

class QuizActivity : AppCompatActivity(), OnNavigationItemSelectedListener, DrawerLocker {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var quizViewModel: QuizViewModel
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    var source: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readSource()
        bindings()
        initDrawer()
        initNavigation()
        startQuiz()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    private fun readSource() {
        // read if there is a source which invoked the quiz
        source = intent.getStringExtra("source")
    }

    private fun bindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)
        drawerLayout = binding.drawerLayoutQuiz
        toolbar = binding.appBarQuiz.toolbar as Toolbar
        navigationView = binding.navigationViewQuiz
        quizViewModel = ViewModelProviders.of(this)[QuizViewModel::class.java]
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
        // loads all menu actions if source is preferences
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.drawer_menu_onlyexit)
    }

    private fun initNavigation() {
        // set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        val bindingAppBar: AppBarHeaderBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.app_bar_header, navigationView, false
        )
        // set up navigation controller and navigation view bindings
        val user = FirebaseAuth.getInstance().currentUser
        // load user name
        bindingAppBar.textviewAppBarHeaderName.text = user?.displayName
        // load user picture
        Picasso.get()
            .load(user?.photoUrl)
            .transform(CircleTransformation())
            .placeholder(R.drawable.ic_user_placeholder_24dp)
            .error(R.drawable.ic_user_placeholder_24dp)
            .fit()
            .into(bindingAppBar.imageviewAppBarHeaderPicture)
        // set up navigation controller and navigation view bindings
        navController = Navigation.findNavController(this,
            R.id.navhostfragment_quiz
        )
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
            Navigation.findNavController(this, R.id.navhostfragment_quiz),
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

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode = if (enabled) LOCK_MODE_UNLOCKED else LOCK_MODE_LOCKED_CLOSED
        drawerLayout.setDrawerLockMode(lockMode)
        drawerToggle.isDrawerIndicatorEnabled = enabled
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
        }
        return true
    }

    private fun startQuiz() {
        val navHostFragment = navhostfragment_quiz as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.quiz_navigation)
        graph.startDestination = R.id.quizInfoFragment
        navHostFragment.navController.graph = graph
    }

}