package com.miramir.mahfaze.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.miramir.mahfaze.ui.login.LoginActivity
import com.miramir.mahfaze.ui.main.MainActivity
import com.miramir.mahfaze.util.PreferencesManager

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PreferencesManager.getUserId(applicationContext) != -1) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish()
    }
}