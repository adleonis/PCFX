package org.pcfx.pdv.androidpdv1.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.pcfx.pdv.androidpdv1.R
import org.pcfx.pdv.androidpdv1.data.PdvPreferences

class MainActivity : AppCompatActivity() {
    private lateinit var contentContainer: android.widget.FrameLayout
    private lateinit var drawerMenu: LinearLayout
    private lateinit var drawerOverlay: View
    private lateinit var hamburgerMenu: Button

    private val scope = CoroutineScope(Dispatchers.Main)
    private var isDrawerOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentContainer = findViewById(R.id.contentContainer)
        drawerMenu = findViewById(R.id.drawerMenu)
        drawerOverlay = findViewById(R.id.drawerOverlay)
        hamburgerMenu = findViewById(R.id.hamburgerMenu)

        setupNavigation()
        checkAndStartServerIfNeeded()

        if (savedInstanceState == null) {
            showDashboard()
        }
    }

    private fun setupNavigation() {
        hamburgerMenu.setOnClickListener {
            toggleDrawer()
        }

        drawerOverlay.setOnClickListener {
            closeDrawer()
        }

        findViewById<LinearLayout>(R.id.navDashboard).setOnClickListener {
            showDashboard()
            closeDrawer()
        }

        findViewById<LinearLayout>(R.id.navSetup).setOnClickListener {
            showSetup()
            closeDrawer()
        }

        findViewById<LinearLayout>(R.id.navAbout).setOnClickListener {
            showAbout()
            closeDrawer()
        }
    }

    private fun toggleDrawer() {
        if (isDrawerOpen) {
            closeDrawer()
        } else {
            openDrawer()
        }
    }

    private fun openDrawer() {
        isDrawerOpen = true
        drawerMenu.visibility = View.VISIBLE
        drawerOverlay.visibility = View.VISIBLE

        val slideIn = TranslateAnimation(-250f, 0f, 0f, 0f)
        slideIn.duration = 300
        drawerMenu.startAnimation(slideIn)

        drawerOverlay.animate()
            .alpha(0.5f)
            .duration = 300
    }

    private fun closeDrawer() {
        isDrawerOpen = false
        val slideOut = TranslateAnimation(0f, -250f, 0f, 0f)
        slideOut.duration = 300
        slideOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation) {}
            override fun onAnimationEnd(animation: android.view.animation.Animation) {
                drawerMenu.visibility = View.GONE
            }
            override fun onAnimationRepeat(animation: android.view.animation.Animation) {}
        })
        drawerMenu.startAnimation(slideOut)

        drawerOverlay.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    drawerOverlay.visibility = View.GONE
                }
            })
    }

    private fun showDashboard() {
        replaceFragment(DashboardFragment(), "dashboard")
        updateHeaderTitle("PDV Dashboard")
    }

    private fun showSetup() {
        replaceFragment(SetupFragment(), "setup")
        updateHeaderTitle("PDV Setup")
    }

    private fun showAbout() {
        replaceFragment(AboutFragment(), "about")
        updateHeaderTitle("PDV About")
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun updateHeaderTitle(title: String) {
        findViewById<android.widget.TextView>(R.id.headerTitle).text = title
    }

    private fun checkAndStartServerIfNeeded() {
        val prefs = PdvPreferences.getInstance(this)
        if (prefs.isAutoStartEnabled()) {
            scope.launch {
                val intent = android.content.Intent(this@MainActivity, org.pcfx.pdv.androidpdv1.service.PdvServerService::class.java).apply {
                    action = org.pcfx.pdv.androidpdv1.service.PdvServerService.ACTION_START_SERVER
                }
                startService(intent)
            }
        }
    }
}
