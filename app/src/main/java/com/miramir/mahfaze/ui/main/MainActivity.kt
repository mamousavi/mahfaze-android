package com.miramir.mahfaze.ui.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.miramir.mahfaze.R
import com.miramir.mahfaze.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.main_nav_host_fragment)
        setupActionBarWithNavController(navController)
        navController.addOnNavigatedListener { _, destination ->
            if (destination.id == R.id.note_detail) {
                supportActionBar?.setDisplayShowTitleEnabled(false)
            } else {
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
        }
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.main_nav_host_fragment).navigateUp()
}
