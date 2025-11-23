package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.app_celushop.R
import com.app_celushop.database.UsuariosDAO
import com.app_celushop.fragments.CarritoFragment
import com.app_celushop.fragments.CatalogoAdministradorFragment
import com.app_celushop.fragments.CatalogoFragment
import com.app_celushop.fragments.EdicionInformacionUsuarioFragment
import com.app_celushop.fragments.GoogleMapsFragment
import com.app_celushop.fragments.PerfilFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var usuariosDAO: UsuariosDAO
    private var correoLogueado: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupNavigationDrawer()
        loadFragment(PerfilFragment())
        usuariosDAO = UsuariosDAO(this)

        val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
        correoLogueado = prefs.getString("correo_usuario", null)
    }
    private fun initViews() {

        drawerLayout = findViewById(R.id.navegacion_menu)
        navView = findViewById(R.id.nav_view)
        findViewById<android.widget.ImageView>(R.id.ic_menu).setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }

    private fun setupNavigationDrawer() {
        navView.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
        when (item.itemId) {
            R.id.nav_perfil -> {
                // Redireccionamiento
                loadFragment(PerfilFragment())
            }
            R.id.nav_sedes -> {
                loadFragment(GoogleMapsFragment())
            }
            R.id.nav_catalogo -> {
                loadFragment(CatalogoFragment())
            }
            R.id.nav_carrito -> {
                loadFragment(CarritoFragment())
            }
            R.id.nav_administrar-> {
                if(correoLogueado != null) {
                    val usuario = usuariosDAO.obtenerUsuario(correoLogueado!!)
                    Log.d("Rol Debug", "Correo del usuario: ${usuario?.email}")
                    Log.d("Rol Debug", "Rol del usuario: ${usuario?.rol}")
                    if (usuario?.rol?.equals("admin", ignoreCase = true) == true) {
                        loadFragment(CatalogoAdministradorFragment())
                    } else {
                        Toast.makeText(this, "No tienes permisos para acceder a esta sección", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No hay usuario logueado", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.nav_cerrarSesion -> {
                FirebaseAuth.getInstance().signOut()
                prefs.edit().clear().apply()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.END)
        return true;
    }

    fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}