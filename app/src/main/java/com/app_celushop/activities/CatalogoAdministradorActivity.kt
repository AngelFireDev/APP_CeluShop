package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.app_celushop.R

class CatalogoAdministradorActivity : AppCompatActivity() {
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_administrador)

        btnAdd = findViewById(R.id.btnAddProduct)
        btnAdd.setOnClickListener{
            val intent = Intent(this, AnadirProductoActivity::class.java)
            startActivity(intent)
        }
    }
}