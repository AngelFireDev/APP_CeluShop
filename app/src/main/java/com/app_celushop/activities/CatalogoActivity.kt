package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.activities.MainActivity
import com.app_celushop.R
import com.app_celushop.activities.adapter.ProductoCatalogoAdapter
import com.app_celushop.activities.adapter.ProductoCatalogoAdminAdapter
import com.app_celushop.database.ProductoDAO
import com.app_celushop.database.UsuariosDAO

class CatalogoActivity : AppCompatActivity() {
    private lateinit var producto: TextView

    private val ProductoDAO = ProductoDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        initViews()
        setupListeners()

    }

    private fun initViews() {
        //producto = findViewById(R.id.titulo_producto)
    }

    private fun setupListeners() {

        verProducto()
/*
        producto.setOnClickListener{
            val intent = Intent(this, DetallesProductoActivity::class.java)
            startActivity(intent)
        }

 */
    }

    private fun verProducto() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerCatalogo)
        recyclerView?.layoutManager = GridLayoutManager(this,  2)

        // 2. Obtener la lista y convertirla a MutableList (NECESARIO para la eliminación local en el Adapter)
        val productosMutableList = ProductoDAO.obtenerTodosLosProductos().toMutableList()
        Log.d("Consola para ver todos la información aquí", "Tamaño de la lista de productos: ${productosMutableList.size}")

        // 3. Crear el Adapter, pasando AMBOS ARGUMENTOS REQUERIDOS:
        val adapter = ProductoCatalogoAdapter(productosMutableList, { productoClickeado ->

            // Crear el Intent para ir a la Activity de Detalles
            val intent = Intent(this, DetallesProductoActivity::class.java).apply {
                // Pasar el objeto Producto completo
                putExtra("PRODUCTO_SELECCIONADO", productoClickeado)
            }
            startActivity(intent)
        } )

        recyclerView?.adapter = adapter
    }

        /*

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


         */

}