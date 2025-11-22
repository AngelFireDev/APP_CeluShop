package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.app_celushop.R
import com.app_celushop.database.UsuariosDAO
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupNavigationDrawer()
        loadFragment(PerfilFragment())
        usuariosDAO = UsuariosDAO(this)
    }
    //val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
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

            }
            R.id.nav_administrar-> {
                loadFragment(CatalogoAdministradorFragment())
            }
            R.id.nav_cerrarSesion -> {
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