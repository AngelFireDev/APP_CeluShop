package com.app_celushop.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.activities.MainActivity
import com.app_celushop.R
import com.app_celushop.database.DatabaseHelper
import com.app_celushop.database.UsuariosDAO
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream

class EdicionInformacionUsuarioActivity : AppCompatActivity() {
    private lateinit var btnSave: Button
    private lateinit var btnChangeDelivery: Button
    private lateinit var btnCancel: Button
    private lateinit var imgPerfil: ImageView
    private lateinit var btnCamara: Button
    private lateinit var btnGaleria: Button
    private lateinit var etCorreo: EditText
    private lateinit var etNombre: EditText
    private lateinit var etTelefono: EditText

    //Camara
    private val tomarFoto = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imgPerfil.setImageBitmap(bitmap)

            //Guardar Imagen
            val ruta = guardarImagen(bitmap)
            val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
            prefs.edit().putString("foto_perfil", ruta).apply()
        }
    }
    private val buscarImagen = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imgPerfil.setImageURI(uri)

            // Copiar la imagen
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "foto_perfil.png")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()

            // Guardar la ruta del archivo interno
            val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
            prefs.edit().putString("foto_perfil", file.absolutePath).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicion_informacion_usuario)

        imgPerfil = findViewById(R.id.img_perfil)
        btnCamara = findViewById(R.id.btn_camara)
        btnGaleria = findViewById(R.id.btn_galeria)
        btnSave = findViewById(R.id.btn_save)
        btnChangeDelivery = findViewById(R.id.btn_chance_delivery)
        btnCancel = findViewById(R.id.btn_cancel)
        etCorreo = findViewById(R.id.et_correo)
        etNombre = findViewById(R.id.et_nombre)

        // 🔹 Mostrar datos desde BD
        val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
        val correoUsuario = prefs.getString("correo_usuario", null)
        if (correoUsuario != null) {
            val usuariosDAO = UsuariosDAO(this)
            val usuario = usuariosDAO.obtenerUsuario(correoUsuario)
            if (usuario != null) {
                etNombre.setText(usuario.nombre)
                etCorreo.setText(usuario.email)
                if (!usuario.foto.isNullOrEmpty()) {
                    val file = File(usuario.foto)
                    Log.d("FOTO_DEBUG", "Ruta en editar: ${usuario.foto}")
                    Log.d("FOTO_DEBUG", "Existe archivo: ${file.exists()}")

                    Glide.with(this)
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
            val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
            val correoUsuario = prefs.getString("correo_usuario", null)
            val rutaFoto = prefs.getString("foto_perfil", null)

            if (!nuevoNombre.isNullOrEmpty() && !correoUsuario.isNullOrEmpty()) {
                val usuariosDAO = UsuariosDAO(this)
                usuariosDAO.actualizarUsuario(correoUsuario, nuevoNombre, rutaFoto)
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
            }
            setResult(RESULT_OK)
            finish()
        }

        btnChangeDelivery.setOnClickListener{
            val intent = Intent(this, EdicionDireccionActivity::class.java)
            startActivity(intent)
        }

        btnCancel.setOnClickListener{
            finish()
        }

        //Camara
        btnCamara.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
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
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarImagen(bitmap: Bitmap): String {
        val filename = "foto_perfil.png"
        val file = File(filesDir, filename)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file.absolutePath
    }
}