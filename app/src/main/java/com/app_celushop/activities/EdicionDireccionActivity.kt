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

class EdicionDireccionActivity : AppCompatActivity() {
    private lateinit var btnChangeDelivery: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicion_direccion)

        btnChangeDelivery = findViewById(R.id.btn_chance_delivery)
        btnCancel = findViewById(R.id.btn_cancel)


        btnChangeDelivery.setOnClickListener {
            //(Funcion para cambiar los datos)
        }

        btnCancel.setOnClickListener{
            finish()
        }
    }
}