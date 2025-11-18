package com.app_celushop.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.R
import com.app_celushop.database.UsuariosDAO

class MenuLateralAdministradorActivity : AppCompatActivity() {

    private val usuariosDAO = UsuariosDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Traer Correo
        val correologueado = intent.getStringExtra("correo_usuario")
        val usuario = usuariosDAO.obtenerUsuario(correologueado ?: "")


        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_lateral_administrador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}