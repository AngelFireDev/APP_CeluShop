package com.app_celushop.fragments

import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app_celushop.R
import com.app_celushop.activities.EdicionDireccionActivity
import com.app_celushop.activities.MainActivity
import com.app_celushop.activities.PerfilActivity
import com.app_celushop.database.UsuariosDAO
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream

class EdicionInformacionUsuarioFragment: Fragment() {
    private lateinit var btnSave: Button
    private lateinit var btnChangeDelivery: Button
    private lateinit var btnCancel: Button
    private lateinit var imgPerfil: ImageView
    private lateinit var btnCamara: Button
    private lateinit var btnGaleria: Button
    private lateinit var etCorreo: EditText
    private lateinit var etNombre: EditText
    private lateinit var etTelefono: EditText
    private val tomarFoto = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imgPerfil.setImageBitmap(bitmap)

            //Guardar Imagen
            val ruta = guardarImagen(bitmap)
            val prefs = requireActivity().getSharedPreferences("sesion_usuario", MODE_PRIVATE)
            prefs.edit().putString("foto_perfil", ruta).apply()
        }
    }
    private val buscarImagen = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val inputStream = requireActivity().contentResolver.openInputStream(uri)
            val filename = "foto_perfil_${System.currentTimeMillis()}.png"
            val file = File(requireActivity().filesDir, filename)

            if (inputStream != null) {
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                outputStream.close()
                inputStream.close()

                val prefs = requireActivity().getSharedPreferences("sesion_usuario", MODE_PRIVATE)
                prefs.edit().putString("foto_perfil", file.absolutePath).apply()

                imgPerfil.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath)) // opcional para mostrarla
                Log.d("FOTO_DEBUG", "Imagen copiada a: ${file.absolutePath}")
            } else {
                Log.e("FOTO_DEBUG", "No se pudo abrir el InputStream del URI")
                Toast.makeText(requireActivity(), "Error al cargar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usa el XML de tu activity_perfil (asumiendo que lo renombraste a fragment_perfil.xml)
        return inflater.inflate(R.layout.fragment_edicion_informacion_usuario, container, false) // ⬅️ Usa el nombre de tu XML de perfil
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgPerfil = view.findViewById(R.id.img_perfil)
        btnCamara = view.findViewById(R.id.btn_camara)
        btnGaleria = view.findViewById(R.id.btn_galeria)
        btnSave = view.findViewById(R.id.btn_save)
        btnChangeDelivery = view.findViewById(R.id.btn_chance_delivery)
        btnCancel = view.findViewById(R.id.btn_cancel)
        etCorreo = view.findViewById(R.id.et_correo)
        etNombre = view.findViewById(R.id.et_nombre)

        // 🔹 Mostrar datos desde BD
        val prefs = requireActivity().getSharedPreferences("sesion_usuario", MODE_PRIVATE)
        val correoUsuario = prefs.getString("correo_usuario", null)
        if (correoUsuario != null) {
            val usuariosDAO = UsuariosDAO(requireActivity())
            val usuario = usuariosDAO.obtenerUsuario(correoUsuario)
            if (usuario != null) {
                etNombre.setText(usuario.nombre)
                etCorreo.setText(usuario.email)
                if (!usuario.foto.isNullOrEmpty()) {
                    val file = File(usuario.foto)
                    Log.d("FOTO_DEBUG", "Ruta en editar: ${usuario.foto}")
                    Log.d("FOTO_DEBUG", "Existe archivo: ${file.exists()}")

                    Glide.with(requireActivity())
                        .load(file)
                        .placeholder(R.drawable.ic_perfil_foto)
                        .error(R.drawable.ic_perfil_foto)
                        .into(imgPerfil)
                } else {
                    imgPerfil.setImageResource(R.drawable.ic_perfil_foto)
                }

            }
        }

        // Guardar
        btnSave.setOnClickListener{
            val nuevoNombre = etNombre.text.toString()
            val prefs = requireActivity().getSharedPreferences("sesion_usuario", MODE_PRIVATE)
            val correoUsuario = prefs.getString("correo_usuario", null)
            val rutaFoto = prefs.getString("foto_perfil", null)

            if (!nuevoNombre.isNullOrEmpty() && !correoUsuario.isNullOrEmpty()) {
                val usuariosDAO = UsuariosDAO(requireActivity())
                usuariosDAO.actualizarUsuario(correoUsuario, nuevoNombre, rutaFoto)
                Toast.makeText(requireActivity(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
            }

            //Funcion para guardar los cambios del usuario
            requireActivity().supportFragmentManager.popBackStack()
        }

        btnChangeDelivery.setOnClickListener{
            //(requireActivity() as MainActivity).loadFragment(EdicionDireccionFragment())
        }

        btnCancel.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        //Camara
        btnCamara.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    100
                )
            } else {
                tomarFoto.launch(null)
            }
        }
        //Buscar Imagenes
        btnGaleria.setOnClickListener {
            buscarImagen.launch("image/*")
        }
    }

    // Manejo de respuesta de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            tomarFoto.launch(null)
        } else {
            Toast.makeText(requireActivity(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarImagen(bitmap: Bitmap): String {
        val filename = "foto_perfil_${System.currentTimeMillis()}.png"
        val file = File(requireActivity().filesDir, filename)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file.absolutePath
    }
}