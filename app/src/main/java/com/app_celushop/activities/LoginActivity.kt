package com.app_celushop.activities

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCaller
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.activities.MainActivity
import com.app_celushop.R
import com.app_celushop.database.UsuariosDAO
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var et_correo: EditText
    private lateinit var et_contrasena: EditText
    private lateinit var btn_login: Button
    private lateinit var register_now: TextView
    private lateinit var usuariosDAO: UsuariosDAO
    private lateinit var btnGoogle: Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    companion object{
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleSignIn"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Inicializar DAO
        usuariosDAO = UsuariosDAO(this)
        //Inicializar Vistas
        initViews()
        //Setup
        setupListeners()
        //Inicio por Google
        btnGoogle = findViewById(R.id.btn_google)
        auth = FirebaseAuth.getInstance() //Inicializar el FireBase Auth

        //Configuración del Sign In Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("430618727924-bq1mt11qv5d5oj3agkil27m94dqbldv6.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnGoogle.setOnClickListener {
            signIn()
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult( requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    // Guardar sesión en SharedPreferences
                    val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
                    prefs.edit()
                        .putString("correo_usuario", currentUser?.email) // 👈 esto es clave
                        .putString("tipo_login", "google")
                        .apply()

                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
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

        if (correo.isEmpty() || contrasena.isEmpty()) {
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

            // Guardar sesión en SharedPreferences
            val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
            prefs.edit()
                .putString("correo_usuario", usuario?.email)
                .putString("tipo_login", "bd")
                .apply()

            Toast.makeText(this, "Bienvenido ${usuario?.nombre}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
        }
    }
}