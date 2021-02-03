package com.example.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

lateinit var bottomNavView : BottomNavigationView
lateinit var navHostFragment : NavHostFragment

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        bottomNavView = findViewById(R.id.bottomNavigation)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment2) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavView,navHostFragment.navController)

    }
}