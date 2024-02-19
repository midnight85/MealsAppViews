package com.example.mealsappviews.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.mealsappviews.R
import com.example.mealsappviews.db.MealDatabase
import com.example.mealsappviews.viewModel.HomeViewModel
import com.example.mealsappviews.viewModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this.applicationContext)
        ViewModelProvider(this, HomeViewModelFactory(mealDatabase))[HomeViewModel::class.java]
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        navController = Navigation.findNavController(this, R.id.main_nav_fragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.navigate(R.id.homeFragment)
                    }
                    true
                }

                R.id.favoritesFragment -> {
                    if (navController.currentDestination?.id != R.id.favoritesFragment) {
                        navController.navigate(R.id.favoritesFragment)
                    }
                    true
                }

                else -> false
            }
        }
    }}