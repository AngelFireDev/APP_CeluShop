package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.activities.MainActivity
import com.app_celushop.R

class EdicionInformacionUsuarioActivity : AppCompatActivity() {
    private lateinit var btnSave: Button
    private lateinit var btnChangeDelivery: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicion_informacion_usuario)

        btnSave = findViewById(R.id.btn_save)
        btnChangeDelivery = findViewById(R.id.btn_chance_delivery)
        btnCancel = findViewById(R.id.btn_cancel)

        btnSave.setOnClickListener{
            //Funcion para guardar los cambios del usuario
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        btnChangeDelivery.setOnClickListener{
            val intent = Intent(this, EdicionDireccionActivity::class.java)
            startActivity(intent)
        }

        btnCancel.setOnClickListener{
            finish()
        }
    }
}