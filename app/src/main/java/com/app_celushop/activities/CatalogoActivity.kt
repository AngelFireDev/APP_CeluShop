package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.activities.MainActivity
import com.app_celushop.R
import com.app_celushop.database.UsuariosDAO

class CatalogoActivity : AppCompatActivity() {
    private lateinit var tituloProducto: TextView
    private lateinit var tituloProducto_2: TextView
    private lateinit var tituloProducto_3: TextView
    private lateinit var tituloProducto_4: TextView

    private val usuariosDAO = UsuariosDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        tituloProducto = findViewById(R.id.titulo_producto)
        tituloProducto_2 = findViewById(R.id.titulo_producto_2)
        tituloProducto_3 = findViewById(R.id.titulo_producto_3)
        tituloProducto_4 = findViewById(R.id.titulo_producto_4)


        tituloProducto.setOnClickListener {
            val intent = Intent(this, DetallesProductoActivity::class.java)
            intent.putExtra("@string/text_imagen", tituloProducto.text.toString())
            startActivity(intent)
         }

        tituloProducto_2.setOnClickListener {
            val intent = Intent(this, DetallesProductoActivity::class.java)
            intent.putExtra("tituloProducto_2", tituloProducto.text.toString())
            startActivity(intent)
        }

        tituloProducto_3.setOnClickListener {
            val intent = Intent(this, DetallesProductoActivity::class.java)
            intent.putExtra("@string/text_imagen", tituloProducto.text.toString())
            startActivity(intent)
        }

        tituloProducto_4.setOnClickListener {
            val intent = Intent(this, DetallesProductoActivity::class.java)
            intent.putExtra("tituloProducto", tituloProducto.text.toString())
            startActivity(intent)
        }
    }
}