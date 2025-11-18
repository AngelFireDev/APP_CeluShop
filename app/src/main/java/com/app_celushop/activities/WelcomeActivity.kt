package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.activities.MainActivity
import com.app_celushop.R
import android.widget.Button

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val btnWelcome = findViewById<Button>(R.id.btn_welcome)




            btnWelcome.setOnClickListener {
                val sharedPref = getSharedPreferences("sesion", MODE_PRIVATE)
                val correo = sharedPref.getString("correo", null)

                if (correo != null) {
                    //Usuario ya tiene sesión abierta
                    val intent = Intent(this, PerfilActivity::class.java)
                    intent.putExtra("correo", correo)
                    startActivity(intent)
                    finish()
                }else {
                    //Sin sesión
                    val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }
}
