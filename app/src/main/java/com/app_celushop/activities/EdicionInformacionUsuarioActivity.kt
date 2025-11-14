package com.app_celushop.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
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
import java.io.File
import java.io.FileOutputStream

class EdicionInformacionUsuarioActivity : AppCompatActivity() {
    private lateinit var btnSave: Button
    private lateinit var btnChangeDelivery: Button
    private lateinit var btnCancel: Button
    private lateinit var imgPerfil: ImageView
    private lateinit var btnCamara: Button
    private lateinit var btnGaleria: Button
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

            val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
            prefs.edit().putString("foto_perfil", uri.toString()).apply()
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

        btnSave.setOnClickListener{
            //Funcion para guardar los cambios del usuario
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
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