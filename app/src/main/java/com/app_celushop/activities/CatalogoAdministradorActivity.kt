package com.app_celushop.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.activities.adapter.ProductoCatalogoAdminAdapter
import com.app_celushop.database.ProductoDAO
import com.app_celushop.models.Producto

class CatalogoAdministradorActivity : AppCompatActivity() {
    private lateinit var btnAdd: Button

    private val ProductoDAO = ProductoDAO(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_administrador)
        //Inicializar DAO
        val ProductoDAO = ProductoDAO(this)

        initViews()
        setupListeners()

    }

    private fun initViews() {
        btnAdd = findViewById(R.id.btnAddProduct)
    }

    private fun setupListeners() {

        verProducto()

        btnAdd.setOnClickListener{
            val intent = Intent(this, AnadirProductoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verProducto() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerCatalogoAdmin)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = ProductoCatalogoAdminAdapter(ProductoDAO.obtenerTodosLosProductos())
    }


}