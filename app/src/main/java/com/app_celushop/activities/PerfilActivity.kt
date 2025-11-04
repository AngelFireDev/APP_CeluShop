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
import android.widget.TextView
import com.app_celushop.database.UsuariosDAO

class PerfilActivity : AppCompatActivity() {
    private lateinit var iv_nombre_user: TextView
    private lateinit var iv_correo_user: TextView
    private lateinit var historial: TextView
    private lateinit var carrito: TextView
    private lateinit var home: TextView
    private lateinit var catalogo: TextView
    private lateinit var btn_cerrar_sesion: Button
    private val usuariosDAO = UsuariosDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        iv_nombre_user = findViewById(R.id.iv_nombre_user)
        iv_correo_user = findViewById(R.id.iv_correo_user)
        btn_cerrar_sesion = findViewById(R.id.btn_cerrar_sesion)
        home = findViewById(R.id.home)
        catalogo = findViewById(R.id.catalogo)
        carrito = findViewById(R.id.carrito)
        historial = findViewById(R.id.historial)

        //Obtener correo
        val correologueado = intent.getStringExtra("correo_usuario")
        val usuario = usuariosDAO.obtenerUsuario(correologueado ?: "")

        //Mostrar Datos
        if (usuario !=null) {
            iv_nombre_user.text = usuario.nombre
            iv_correo_user.text = usuario.email
        }

        //Home
        home.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Catalogo
        catalogo.setOnClickListener {
            val intent = Intent(this, CatalogoActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Carrito
        carrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Cerrar Sesión
        btn_cerrar_sesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}