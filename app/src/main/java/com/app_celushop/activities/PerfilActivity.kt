package com.app_celushop.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.app_celushop.R
import android.widget.TextView
import com.app_celushop.database.UsuariosDAO
import java.io.File

class PerfilActivity : AppCompatActivity() {
    private lateinit var iv_nombre_user: TextView
    private lateinit var iv_correo_user: TextView
    private lateinit var ed_perfil: TextView
    private lateinit var carrito: TextView
    private lateinit var home: TextView
    private lateinit var catalogo: TextView
    private lateinit var administrar: TextView
    private lateinit var btn_cerrar_sesion: Button
    private lateinit var img_perfil: ImageView
    private val usuariosDAO = UsuariosDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        img_perfil = findViewById(R.id.img_perfil)
        iv_nombre_user = findViewById(R.id.iv_nombre_user)
        iv_correo_user = findViewById(R.id.iv_correo_user)
        btn_cerrar_sesion = findViewById(R.id.btn_cerrar_sesion)
        home = findViewById(R.id.home)
        catalogo = findViewById(R.id.catalogo)
        carrito = findViewById(R.id.carrito)
        administrar = findViewById(R.id.administrador)
        ed_perfil = findViewById(R.id.edit_perfil)

        val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)

        val ruta = prefs.getString("foto_perfil", null)

        if (ruta != null) {
            val file = File(ruta)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(ruta)
                img_perfil.setImageBitmap(bitmap)
            } else {
                img_perfil.setImageResource(R.drawable.ic_perfil_foto)
            }
        }

        //Obtener correo
        val correologueado = intent.getStringExtra("correo_usuario")
        val usuario = usuariosDAO.obtenerUsuario(correologueado ?: "")

        //Mostrar Datos
        if (usuario != null) {
            iv_nombre_user.text = usuario.nombre
            iv_correo_user.text = usuario.email
        }

        //Home
        home.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        //Editar perfil
        ed_perfil.setOnClickListener {
            val intent = Intent(this, EdicionInformacionUsuarioActivity::class.java)
            startActivity(intent)
        }

        //Catalogo
        catalogo.setOnClickListener {
            val intent = Intent(this, CatalogoActivity::class.java)
            startActivity(intent)
        }

        //Administrador
        administrar.setOnClickListener {
            val intent = Intent(this, CatalogoAdministradorActivity::class.java)
            startActivity(intent)
        }

        //Carrito
        carrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
        }

        //Cerrar Sesión
        btn_cerrar_sesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}