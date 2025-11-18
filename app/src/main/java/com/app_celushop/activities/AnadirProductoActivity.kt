package com.app_celushop.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app_celushop.activities.MainActivity
import com.app_celushop.R
import com.app_celushop.database.ProductoDAO
import com.app_celushop.database.UsuariosDAO
import com.app_celushop.models.Producto
import com.app_celushop.models.Usuarios

class AnadirProductoActivity : AppCompatActivity() {

    private lateinit var et_producto: EditText
    private lateinit var et_descripción: EditText
    private lateinit var et_precio: EditText
    private lateinit var et_imagen: EditText
    private lateinit var et_marca: EditText
    private lateinit var et_modelo: EditText
    private lateinit var et_almacenamiento: EditText
    private lateinit var et_ram: EditText
    private lateinit var et_color: EditText
    private lateinit var et_stock: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnCancel: Button

    private var productoAEditar: Producto? = null

    private val ProductoDAO = ProductoDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_producto)

        //Inicializar DAO
        val ProductoDAO = ProductoDAO(this)

        initViews()
        setupListeners()

    }

    private fun setupListeners() {
        btnAdd.setOnClickListener {
            registrarProducto()
        }

        recibirDatosProducto()

        btnCancel.setOnClickListener{
            finish()
        }
    }

    private fun recibirDatosProducto() {
        productoAEditar = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 (API 33) y superior
            intent.getParcelableExtra("PRODUCTO_A_EDITAR", Producto::class.java)
        } else {
            // Para versiones anteriores
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("PRODUCTO_A_EDITAR")
        }


        if (productoAEditar != null) {
            // ... (Tu lógica para rellenar campos) ...
            rellenarCampos(productoAEditar!!)
            // ...
        }
    }

    private fun rellenarCampos(producto: Producto) {
        // Rellenar todos los campos del formulario
        findViewById<EditText>(R.id.productName).setText(producto.nombre)
        findViewById<EditText>(R.id.productDescription).setText(producto.descripcion)
        findViewById<EditText>(R.id.productPrice).setText(producto.precio.toString())
        findViewById<EditText>(R.id.productImage).setText(producto.url_imagen)
        findViewById<EditText>(R.id.productBrand).setText(producto.marca)
        findViewById<EditText>(R.id.productModel).setText(producto.modelo)
        findViewById<EditText>(R.id.productStorage).setText(producto.almacenamiento)
        findViewById<EditText>(R.id.productRam).setText(producto.ram)
        findViewById<EditText>(R.id.productColor).setText(producto.color)
        findViewById<EditText>(R.id.productStock).setText(producto.stock.toString())

        // Nota: Si tienes campos de imagen, también debes cargarlos aquí (ej. usando Glide/Picasso)
    }

    private fun initViews() {
        et_producto = findViewById(R.id.productName)
        et_descripción = findViewById(R.id.productDescription)
        et_precio = findViewById(R.id.productPrice)
        et_imagen = findViewById(R.id.productImage)
        et_marca = findViewById(R.id.productBrand)
        et_modelo = findViewById(R.id.productModel)
        et_almacenamiento = findViewById(R.id.productStorage)
        et_ram = findViewById(R.id.productRam)
        et_color = findViewById(R.id.productColor)
        et_stock = findViewById(R.id.productStock)
        btnAdd = findViewById(R.id.btn_save)
        btnCancel = findViewById(R.id.btn_cancel)
    }

    private fun registrarProducto() {
        val nombre_producto = et_producto.text.toString().trim()
        val descripción = et_descripción.text.toString().trim()
        val precioString = et_precio.text.toString().trim()
        val precio: Int = precioString.toInt()
        val imagen = et_imagen.text.toString().trim()
        val marca = et_marca.text.toString().trim()
        val modelo = et_modelo.text.toString().trim()
        val almacenamiento = et_almacenamiento.text.toString().trim()
        val ram = et_ram.text.toString().trim()
        val color = et_color.text.toString().trim()
        val stockSting = et_stock.text.toString().trim()
        val stock: Int = stockSting.toInt()

        //Validar Campos
        when {
            // Ningun Campo Vacio
            nombre_producto.isEmpty() || descripción.isEmpty() || precioString.isEmpty() || imagen.isEmpty() || marca.isEmpty() || modelo.isEmpty() || almacenamiento.isEmpty() || ram.isEmpty() || color.isEmpty() || stockSting.isEmpty() -> {
                Toast.makeText(this, "Por favor completa los campos", Toast.LENGTH_SHORT).show()
                return
            }


        }

        //Si pasa todas las condiciones
        var producto = Producto(nombre = nombre_producto, descripcion = descripción, precio = precio, url_imagen = imagen, marca = marca, modelo = modelo, almacenamiento = almacenamiento, ram = ram, color = color, stock = stock)

        if (productoAEditar != null) {
            // MODO EDICIÓN: Asignar el ID original y actualizar
            producto.id_producto = productoAEditar!!.id_producto // Usar el ID que se recibió

            // Usar la función que creaste en ProductoDAO para actualizar
            val actualizadoExitosamente = ProductoDAO.actualizarProducto(producto)

            if (actualizadoExitosamente) {
                Toast.makeText(this, "Producto actualizado exitosamente.", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish() // Cierra la Activity y regresa al catálogo
            } else {
                Toast.makeText(this, "Error al actualizar el producto.", Toast.LENGTH_SHORT).show()
            }
        }else{
            val agregado = ProductoDAO.añadirProducto(producto)

            if(agregado) {
                Toast.makeText(this, "Producto registrado exitosamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CatalogoAdministradorActivity::class.java)
                setResult(Activity.RESULT_OK)
                startActivity(intent)

            }else {
                Toast.makeText(this, "Error al registrar producto", Toast.LENGTH_SHORT).show()
            }
        }
    }


}