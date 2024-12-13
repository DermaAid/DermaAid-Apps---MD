package com.example.dermaaid.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dermaaid.R
import com.example.dermaaid.ui.CameraFragment
import com.example.dermaaid.ui.History
import com.example.dermaaid.ui.HomeFragment
import com.example.dermaaid.ui.NewsFragment
import com.example.dermaaid.ui.Profile
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var navview: BottomNavigationView
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        navview = findViewById(R.id.nav_view)
        fab = findViewById(R.id.fab)

        // Set default fragment
        replace(HomeFragment())

        // Handle Bottom Navigation Item Selection
        navview.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> replace(HomeFragment())
                R.id.navigation_news -> replace(NewsFragment())
                R.id.navigation_history -> replace(History())
                R.id.navigation_profile -> replace(Profile())
                else -> false
            }
            true
        }

        // Handle Floating Action Button Click
        fab.setOnClickListener {
            replace(CameraFragment())
        }
    }

    private fun replace(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navhost, fragment)
        fragmentTransaction.commit()
    }
}
