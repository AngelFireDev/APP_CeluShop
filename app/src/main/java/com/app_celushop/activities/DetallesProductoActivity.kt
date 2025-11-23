package com.app_celushop.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.activities.MainActivity
import com.app_celushop.R
import com.app_celushop.database.CarritoDAO
import com.app_celushop.models.Producto
import com.bumptech.glide.Glide

class DetallesProductoActivity : AppCompatActivity() {
    private lateinit var btn_carrito: Button
    private lateinit var btn_comprar: Button

    private lateinit var carritoDAO:CarritoDAO

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_producto)

        btn_carrito = findViewById(R.id.btn_carrito)
        btn_comprar = findViewById(R.id.btn_comprar)
        carritoDAO = CarritoDAO(context = this)

        val producto: Producto? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("PRODUCTO_SELECCIONADO", Producto::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("PRODUCTO_SELECCIONADO")
        }

        // Usar los datos del producto
        if (producto != null) {
            val textNombre = findViewById<TextView>(R.id.tituloProducto)
            val textPrecio = findViewById<TextView>(R.id.precioProducto)
            val textMarca = findViewById<TextView>(R.id.ValorDatoMarca)
            val textModelo = findViewById<TextView>(R.id.ValorDatoModelo)
            val textAlmacenamiento = findViewById<TextView>(R.id.ValorDatoAlamacenamiento)
            val textRam = findViewById<TextView>(R.id.ValorDatoRam)
            val textColor = findViewById<TextView>(R.id.ValorDatoColor)
            val imageProducto = findViewById<ImageView>(R.id.imagenProducto)

            textNombre.text = producto.nombre
            textPrecio.text = "$${producto.precio}"
            textMarca.text = producto.marca
            textModelo.text = producto.modelo
            textAlmacenamiento.text = producto.almacenamiento
            textRam.text = producto.ram
            textColor.text = producto.color

            Glide.with(this).load(producto.url_imagen).into(imageProducto)
        }

        if (producto != null) {
            btn_carrito.setOnClickListener {
                agregarACarrito(producto)
            }
        }

        btn_comprar.setOnClickListener {
            val intent = Intent(this, ConfirmacionCompraActivity::class.java)
            startActivity(intent)
        }
    }

    private fun agregarACarrito(producto: Producto) {
        val agregado = carritoDAO.añadirCarrito(producto)

        if (agregado) {
            Toast.makeText(this, "✅ '${producto.nombre}' añadido al carrito", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "❌ Error al añadir al carrito o ya existe", Toast.LENGTH_SHORT).show()
        }
    }
}