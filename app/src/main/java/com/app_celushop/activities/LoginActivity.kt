package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.MainActivity
import com.app_celushop.R
import com.app_celushop.database.UsuariosDAO
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var et_correo: EditText
    private lateinit var et_contrasena: EditText
    private lateinit var btn_login: Button
    private lateinit var register_now: TextView
    private lateinit var usuariosDAO: UsuariosDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Inicializar DAO
        usuariosDAO = UsuariosDAO(this)
        //Inicializar Vistas
        initViews()
        //Setup
        setupListeners()
    }

    private fun initViews() {
        et_correo = findViewById(R.id.et_correo)
        et_contrasena = findViewById(R.id.et_contrasena)
        btn_login = findViewById(R.id.btn_login)
        register_now = findViewById(R.id.register_now)
    }

    private fun setupListeners() {
        btn_login.setOnClickListener {
            iniciarSesion()
        }
        register_now.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesion() {
        val correo = et_correo.text.toString()
        val contrasena = et_contrasena.text.toString()

        if(correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Por favor ingrese un correo válido", Toast.LENGTH_SHORT).show()
            return
        }
        //Validar credenciales en BD
        if (usuariosDAO.validarLogin(correo, contrasena)) {
           val usuario = usuariosDAO.obtenerUsuario(correo)
            Toast.makeText(this, "Bienvenido ${usuario?.nombre}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
        }
    }

}