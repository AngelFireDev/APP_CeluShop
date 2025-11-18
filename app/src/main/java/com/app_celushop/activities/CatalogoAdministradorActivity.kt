package com.app_celushop.activities

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.activities.adapter.ProductoCatalogoAdminAdapter
import com.app_celushop.database.ProductoDAO
import com.app_celushop.models.Producto
import androidx.activity.result.contract.ActivityResultContracts
import com.app_celushop.database.UsuariosDAO

class CatalogoAdministradorActivity : AppCompatActivity() {
    private lateinit var btnAdd: Button
    private lateinit var productoAdapter: ProductoCatalogoAdminAdapter
    private lateinit var productosMutableList: MutableList<Producto>
    private val ProductoDAO = ProductoDAO(this)
    private val usuariosDAO = UsuariosDAO(this)

    private val productoActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Este bloque se ejecuta cuando regresas de AnadirProductoActivity

            // Verifica si la actividad terminó correctamente y si hubo cambios
            if (result.resultCode == Activity.RESULT_OK) {
                // Si el resultado es OK, recarga la lista para ver los cambios.
                verProducto() // Llama a tu función que recarga el Adapter
            }
        }


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
        recyclerView?.layoutManager = LinearLayoutManager(this)

        // 2. Obtener la lista y convertirla a MutableList (NECESARIO para la eliminación local en el Adapter)
        val productosMutableList = ProductoDAO.obtenerTodosLosProductos().toMutableList()
        Log.d("Consola para ver todos la información aquí", "Tamaño de la lista de productos: ${productosMutableList.size}")

        // 3. Crear el Adapter, pasando AMBOS ARGUMENTOS REQUERIDOS:
        val adapter = ProductoCatalogoAdminAdapter(
            productosMutableList,
            { idProductoAEliminar ->

                // Esta es la lógica del onDeleteClickListener:
                val eliminadoExitosamente = ProductoDAO.eliminarProducto(idProductoAEliminar.toString())

                if (eliminadoExitosamente) {
                    Toast.makeText(this, "Producto eliminado correctamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al eliminar de la BD.", Toast.LENGTH_SHORT).show()
                }
            }
        )

        { productoAEditar ->
            val intent = Intent(this, AnadirProductoActivity::class.java)
            // Nota: Asegúrate de que Producto sea Parcelable.
            intent.putExtra("PRODUCTO_A_EDITAR", productoAEditar)
            //startActivity(intent)

            productoActivityResultLauncher.launch(intent)
        }


        recyclerView?.adapter = adapter
    }


}