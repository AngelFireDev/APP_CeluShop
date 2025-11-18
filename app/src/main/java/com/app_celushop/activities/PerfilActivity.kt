package com.app_celushop.activities

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.app_celushop.R
import android.widget.TextView
import android.widget.Toast
import com.app_celushop.database.UsuariosDAO
import com.bumptech.glide.Glide
import java.io.File
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class PerfilActivity : AppCompatActivity() {
    private lateinit var iv_nombre_user: TextView
    private lateinit var iv_correo_user: TextView
    private lateinit var ed_perfil: TextView
    private lateinit var carrito: TextView
    private lateinit var home: TextView
    private lateinit var catalogo: TextView
    private lateinit var administrar: TextView
    private lateinit var sedes: TextView
    private lateinit var btn_cerrar_sesion: Button
    private lateinit var img_perfil: ImageView
    private val usuariosDAO = UsuariosDAO(this)
    private var correoLogueado: String? = null

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
        sedes = findViewById(R.id.sedes)
        ed_perfil = findViewById(R.id.edit_perfil)

        val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)

        correoLogueado = prefs.getString("correo_usuario", null)

        val ruta = prefs.getString("foto_perfil", null)

        if (ruta != null) {
            val file = File(ruta)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(ruta)
                img_perfil.setImageBitmap(bitmap)
            } else {
                val uri = Uri.parse(ruta)
                img_perfil.setImageURI(uri)
            }
        } else {
            img_perfil.setImageResource(R.drawable.ic_perfil_foto)
        }

        val tipoLogin = prefs.getString("tipo_login", null)
        val correoUsuario = prefs.getString("correo_usuario", null)

        if(tipoLogin == "google") {
            // Usuario Firebase
            val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

            if(currentUser != null) {
                //Usuario Google
                iv_nombre_user.text = currentUser.displayName ?: "Usuario"
                iv_correo_user.text = currentUser.email ?: "Correo"

                val photoUrl = currentUser.photoUrl
                if (photoUrl != null) {
                    Glide.with(this)
                        .load(photoUrl)
                        .placeholder(R.drawable.ic_perfil_foto)
                        .error(R.drawable.ic_perfil_foto)
                        .into(img_perfil)
                } else {
                    img_perfil.setImageResource(R.drawable.ic_perfil_foto)
                }
            }
        } else if (tipoLogin == "bd") {
            if (correoUsuario != null) {
                val usuario = usuariosDAO.obtenerUsuario(correoUsuario)
                if (usuario != null) {
                    iv_nombre_user.text = usuario.nombre
                    iv_correo_user.text = usuario.email

                    if (!usuario.foto.isNullOrEmpty()) {
                        val file = File(usuario.foto)
                        Glide.with(this)
                            .load(file)
                            .placeholder(R.drawable.ic_perfil_foto)
                            .error(R.drawable.ic_perfil_foto)
                            .into(img_perfil)
                    }
                }
            }
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
           if(correoLogueado != null) {
               val usuario = usuariosDAO.obtenerUsuario(correoLogueado!!)
               Log.d("Rol Debug", "Correo del usuario: ${usuario?.email}")
               Log.d("Rol Debug", "Rol del usuario: ${usuario?.rol}")
               if (usuario?.rol?.equals("admin", ignoreCase = true) == true) {
                   val intent = Intent(this, CatalogoAdministradorActivity::class.java)
                   startActivity(intent)
               } else {
                   Toast.makeText(this, "No tienes permisos para acceder a esta sección", Toast.LENGTH_SHORT).show()
               }
           } else {
               Toast.makeText(this, "No hay usuario logueado", Toast.LENGTH_SHORT).show()
           }
        }

        //Sedes
        sedes.setOnClickListener {
            val intent = Intent(this, GoogleMaps::class.java)
            startActivity(intent)
        }

        //Carrito
        carrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
        }

        //Cerrar Sesión
        btn_cerrar_sesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            prefs.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}