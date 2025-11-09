package com.app_celushop.activities

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

        btnCancel.setOnClickListener{
            finish()
        }
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

            //Verificar que el producto no este registrado
            ProductoDAO.validarProducto(nombre_producto) -> {
                Toast.makeText(this, "El producto ya esta registrado", Toast.LENGTH_SHORT).show()
                return
            }
        }

        //Si pasa todas las condiciones
        var producto = Producto(nombre = nombre_producto, descripcion = descripción, precio = precio, url_imagen = imagen, marca = marca, modelo = modelo, almacenamiento = almacenamiento, ram = ram, color = color, stock = stock)
        val agregado = ProductoDAO.añadirProducto(producto)

        if(agregado) {
            Toast.makeText(this, "Producto registrado exitosamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CatalogoAdministradorActivity::class.java)
            startActivity(intent)

        }else {
            Toast.makeText(this, "Error al registrar producto", Toast.LENGTH_SHORT).show()
        }
    }


}