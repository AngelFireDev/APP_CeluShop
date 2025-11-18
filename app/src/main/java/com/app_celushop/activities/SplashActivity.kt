package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.app_celushop.activities.MainActivity
import com.app_celushop.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DURATION = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DURATION)
    }
}
