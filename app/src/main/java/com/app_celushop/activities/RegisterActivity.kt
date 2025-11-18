package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.activities.MainActivity
import com.app_celushop.R
import com.app_celushop.database.UsuariosDAO
import com.app_celushop.models.Usuarios
import com.google.android.material.textfield.TextInputEditText
import com.app_celushop.activities.LoginActivity


class RegisterActivity : AppCompatActivity() {
    private lateinit var et_nombre: EditText
    private lateinit var et_correo: EditText
    private lateinit var et_contrasena: EditText
    private lateinit var  et_confirm_contrasena: EditText
    private lateinit var btn_register: Button
    private val usuariosDAO = UsuariosDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val prefs = getSharedPreferences("setup", MODE_PRIVATE)
        val yaEncriptado = prefs.getBoolean("passwords_encriptadas", false)

        //Inicializar DAO
        val usuariosDAO = UsuariosDAO(this)

        initViews()
        setupListeners()
    }

    private fun setupListeners() {
        btn_register.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun initViews() {
        et_nombre = findViewById(R.id.et_nombre)
        et_correo = findViewById(R.id.et_correo)
        et_contrasena = findViewById(R.id.et_contrasena)
        et_confirm_contrasena = findViewById(R.id.et_confirm_contrasena)
        btn_register = findViewById(R.id.btn_register)

    }
    private fun registrarUsuario() {
        val nombre = et_nombre.text.toString().trim()
        val correo = et_correo.text.toString().trim()
        val contrasena = et_contrasena.text.toString().trim()
        val confirmContrasena = et_confirm_contrasena.text.toString().trim()

        //Validar Campos
        when {
            // Ningun Campo Vacio
            nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmContrasena.isEmpty() -> {
                Toast.makeText(this, "Por favor completa los campos", Toast.LENGTH_SHORT).show()
                return
            }

            //Validación Formato
            !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                Toast.makeText(this,"Por favor ingresa un correo valido", Toast.LENGTH_SHORT).show()
                return
            }

            //Validación Contraseña
            contrasena != confirmContrasena -> {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return
            }

            //Validacion de la longitud de contraseña
            contrasena.length < 6 -> {
                Toast.makeText(this,"La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return
            }

            //Verificar que el email no este registrado
            usuariosDAO.validarEmail(correo) -> {
                Toast.makeText(this, "El email ya esta registrado", Toast.LENGTH_SHORT).show()
                return
            }
        }

        //Si pasa todas las condiciones
        var usuario = Usuarios(nombre = nombre, email = correo, password = contrasena, rol = "usuario")
        val registrado = usuariosDAO.registrarUsuario(usuario)

        if(registrado) {
            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
        }
    }
}

