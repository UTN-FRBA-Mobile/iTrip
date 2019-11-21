package com.android.itrip.activities


import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.navigation.ui.NavigationUI
import com.android.itrip.R
import com.android.itrip.databinding.ActivityMainBinding
import com.android.itrip.databinding.AppBarHeaderBinding
import com.android.itrip.dependencyInjection.ContextModule
import com.android.itrip.dependencyInjection.DaggerApiComponent
import com.android.itrip.services.QuizService
import com.android.itrip.util.CircleTransformation
import com.android.itrip.util.RequestCodes.Companion.REQUEST_IMAGE_GET
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar.view.*
import java.util.logging.Logger
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    @Inject
    lateinit var quizService: QuizService

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerApiComponent.builder().contextModule(ContextModule(applicationContext)).build()
            .injectMainActivity(this)
        super.onCreate(savedInstanceState)
        bindings()
        initDrawer()
        initNavigation()
        isQuizAnswered()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    private fun bindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        toolbar = binding.appBar.toolbar as Toolbar
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
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        val bindingAppBar: AppBarHeaderBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.app_bar_header, navigationView, false
        )
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
            R.id.drawer_menu_preferences -> {
                val intent = Intent(this, QuizActivity::class.java).apply {
                    putExtra("source", "preferences")
                }
                startActivity(intent)
            }
        }
        return true
    }

    private fun isQuizAnswered() {
        quizService.getResolution({ answered ->
            if (!answered) {
                val intent = Intent(this, QuizActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, { error ->
            logger.severe("Failed to retrieve quiz result - status: ${error.statusCode} - message: ${error.message}")
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        packageManager
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val thumbnail: Bitmap? = data?.getParcelableExtra("data")
            val fullPhotoUri: Uri? = data?.data
        }
    }

}
