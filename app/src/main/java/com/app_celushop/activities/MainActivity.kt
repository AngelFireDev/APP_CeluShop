package com.app_celushop.activities

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
import com.app_celushop.fragments.PerfilFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var usuariosDAO: UsuariosDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initViews()
        setupNavigationDrawer()
        usuariosDAO = UsuariosDAO(this)
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.main)
        navView = findViewById(R.id.nav_view)
        findViewById<android.widget.ImageView>(R.id.icono).setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun setupNavigationDrawer() {
        navView.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
            }
            R.id.nav_catalogo -> {

            }
            R.id.nav_carrito -> {

            }
            R.id.nav_perfil -> {
                // Redireccionamiento
                loadFragment(PerfilFragment())
            }
            R.id.nav_cerrarSesion -> {

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true;
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}