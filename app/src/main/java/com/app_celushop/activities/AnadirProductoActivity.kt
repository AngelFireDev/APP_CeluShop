package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.app_celushop.activities.MainActivity
import com.app_celushop.R

class AnadirProductoActivity : AppCompatActivity() {
    private lateinit var btnAdd: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_producto)

        btnAdd = findViewById(R.id.btn_save)
        btnCancel = findViewById(R.id.btn_cancel)

        btnAdd.setOnClickListener{
            //funcion para añadir producto
            val intent = Intent(this, CatalogoAdministradorActivity::class.java)
            startActivity(intent)
        }

        btnCancel.setOnClickListener{
            finish()
        }
    }
}