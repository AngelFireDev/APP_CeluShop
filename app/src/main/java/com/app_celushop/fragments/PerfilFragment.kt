package com.app_celushop.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app_celushop.R
import com.app_celushop.activities.CarritoActivity
import com.app_celushop.activities.CatalogoActivity
import com.app_celushop.activities.CatalogoAdministradorActivity
import com.app_celushop.activities.EdicionInformacionUsuarioActivity
import com.app_celushop.activities.GoogleMaps
import com.app_celushop.activities.LoginActivity
import com.app_celushop.activities.MainActivity
import com.app_celushop.activities.WelcomeActivity
import com.app_celushop.database.UsuariosDAO
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.File

class PerfilFragment: Fragment() {
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

    private lateinit var usuariosDAO: UsuariosDAO

    private var correoLogueado: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usuariosDAO = UsuariosDAO(requireContext())

        img_perfil = view.findViewById(R.id.img_perfil)
        iv_nombre_user = view.findViewById(R.id.iv_nombre_user)
        iv_correo_user = view.findViewById(R.id.iv_correo_user)
        btn_cerrar_sesion = view.findViewById(R.id.btn_cerrar_sesion)
        home = view.findViewById(R.id.home)
        catalogo = view.findViewById(R.id.catalogo)
        carrito = view.findViewById(R.id.carrito)
        administrar = view.findViewById(R.id.administrador)
        sedes = view.findViewById(R.id.sedes)
        ed_perfil = view.findViewById(R.id.edit_perfil)

        val prefs = requireActivity().getSharedPreferences("sesion_usuario", MODE_PRIVATE)

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
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
        }

        //Editar perfil
        ed_perfil.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(EdicionInformacionUsuarioFragment())
        }

        //Catalogo
        catalogo.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(CatalogoFragment())
        }

        //Administrador
        administrar.setOnClickListener {
            if(correoLogueado != null) {
                val usuario = usuariosDAO.obtenerUsuario(correoLogueado!!)
                Log.d("Rol Debug", "Correo del usuario: ${usuario?.email}")
                Log.d("Rol Debug", "Rol del usuario: ${usuario?.rol}")
                if (usuario?.rol?.equals("admin", ignoreCase = true) == true) {
                    (requireActivity() as MainActivity).loadFragment(CatalogoAdministradorFragment())
                } else {
                    Toast.makeText(requireContext(), "No tienes permisos para acceder a esta sección", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "No hay usuario logueado", Toast.LENGTH_SHORT).show()
            }
        }

        //Sedes
        sedes.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(GoogleMapsFragment())
        }

        //Carrito
        carrito.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(CarritoFragment())
        }

        //Cerrar Sesión
        btn_cerrar_sesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            prefs.edit().clear().apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}