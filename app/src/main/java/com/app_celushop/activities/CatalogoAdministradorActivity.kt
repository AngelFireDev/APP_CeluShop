package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.app_celushop.R

class CatalogoAdministradorActivity : AppCompatActivity() {
    private val SPLASH_DURATION = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_administrador)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, AnadirProductoActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DURATION)
    }
}