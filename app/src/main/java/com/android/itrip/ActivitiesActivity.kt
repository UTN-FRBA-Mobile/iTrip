package com.android.itrip

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.android.itrip.RequestCodes.Companion.VIEW_ACTIVITY_DETAILS_CODE
import com.android.itrip.databinding.ActivityActivitiesBinding
import com.android.itrip.databinding.AppBarHeaderBinding
import com.android.itrip.models.Actividad
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class ActivitiesActivity : AppCompatActivity(), OnNavigationItemSelectedListener, DrawerLocker {

    private lateinit var binding: ActivityActivitiesBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    private lateinit var actividades: List<Actividad>
    private lateinit var actividad: Actividad
    private var action = 0
    var source: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readSource()
        bindings()
        initDrawer()
        initNavigation()
        startActivitiesList()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    private fun readSource() {
        source = intent.getStringExtra("source")
        action = intent?.extras?.getInt("action") ?: 0
        intent?.extras?.get("actividad")?.let {
            actividad = it as Actividad
        }
        @Suppress("UNCHECKED_CAST")
        intent?.extras?.get("actividades")?.let {
            actividades = it as List<Actividad>
        }
    }

    private fun bindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activities)
        drawerLayout = binding.drawerLayoutActivities
        toolbar = binding.appBarActivities as Toolbar
        navigationView = binding.navigationViewActivities
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
            .placeholder(R.drawable.ic_user_placeholder_24dp)
            .error(R.drawable.ic_user_placeholder_24dp)
            .fit()
            .into(bindingAppBar.imageviewAppBarHeaderPicture)
        navController = Navigation.findNavController(this, R.id.navhostfragment_activities)
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
            Navigation.findNavController(this, R.id.navhostfragment_activities),
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

    private fun startActivitiesList() {
        val navController: NavController
        val bundle: Bundle
        when (action) {
            VIEW_ACTIVITY_DETAILS_CODE -> {
                navController = findNavController(R.id.navhostfragment_activities)
                navController.graph.startDestination = R.id.activityDetailsFragment
                bundle = bundleOf("actividad" to actividad, "action" to action)
                navController.setGraph(navController.graph, bundle)
            }
            else -> {
                navController = findNavController(R.id.navhostfragment_activities)
                bundle = bundleOf("actividades" to actividades)
                navController.setGraph(navController.graph, bundle)
            }
        }
    }

    fun finishActivity(actividad: Actividad) {
        val returnIntent = Intent()
        returnIntent.putExtra("actividad", actividad)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

}