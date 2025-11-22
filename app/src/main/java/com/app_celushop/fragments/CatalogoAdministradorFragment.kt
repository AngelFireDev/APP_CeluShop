package com.app_celushop.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.activities.AnadirProductoActivity
import com.app_celushop.activities.MainActivity
import com.app_celushop.activities.adapter.ProductoCatalogoAdminAdapter
import com.app_celushop.database.ProductoDAO
import com.app_celushop.database.UsuariosDAO
import com.app_celushop.models.Producto

class CatalogoAdministradorFragment: Fragment() {

    private lateinit var btnAdd: Button
    private lateinit var productoAdapter: ProductoCatalogoAdminAdapter
    private lateinit var productosMutableList: MutableList<Producto>
    private var fragmentRootView: View? = null
    private lateinit var ProductoDAO: ProductoDAO

    private val productoActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Este bloque se ejecuta cuando regresas de AnadirProductoActivity

            // Verifica si la actividad terminó correctamente y si hubo cambios
            if (result.resultCode == Activity.RESULT_OK) {
                fragmentRootView?.let {
                    verProducto(it)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usa el XML de tu activity_perfil (asumiendo que lo renombraste a fragment_perfil.xml)
        return inflater.inflate(R.layout.fragment_catalogo_administrador, container, false) // ⬅️ Usa el nombre de tu XML de perfil
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Inicializar DAO
        ProductoDAO = ProductoDAO(requireContext())

        fragmentRootView = view
        initViews(view)
        setupListeners(view)

    }

    private fun initViews(view: View) {
        btnAdd = view.findViewById(R.id.btnAddProduct)
    }

    private fun setupListeners(rootView: View) {

        verProducto(rootView)

        btnAdd.setOnClickListener{
            (requireActivity() as MainActivity).loadFragment(AnadirProductoFragment())
        }
    }

    private fun verProducto(rootView: View) {
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerCatalogoAdmin)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

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
                    Toast.makeText(requireContext(), "Producto eliminado correctamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar de la BD.", Toast.LENGTH_SHORT).show()
                }
            }
        )

        { productoAEditar ->
            val anadirEditarFragment = AnadirProductoFragment()
            val args = Bundle()
            args.putParcelable("PRODUCTO_A_EDITAR", productoAEditar)

            anadirEditarFragment.arguments = args

            (requireActivity() as MainActivity).loadFragment(anadirEditarFragment)
        }


        recyclerView?.adapter = adapter
    }

}