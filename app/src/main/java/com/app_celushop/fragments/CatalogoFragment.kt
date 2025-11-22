package com.app_celushop.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.activities.DetallesProductoActivity
import com.app_celushop.activities.MainActivity
import com.app_celushop.activities.adapter.ProductoCatalogoAdapter
import com.app_celushop.database.ProductoDAO
import com.app_celushop.database.UsuariosDAO

class CatalogoFragment: Fragment() {
    private lateinit var producto: TextView

    private lateinit var ProductoDAO: ProductoDAO



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usa el XML de tu activity_perfil (asumiendo que lo renombraste a fragment_perfil.xml)
        return inflater.inflate(R.layout.fragment_catalogo, container, false) // ⬅️ Usa el nombre de tu XML de perfil
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductoDAO = ProductoDAO(requireContext())

        initViews(view)
        setupListeners(view)

    }

    private fun initViews(view: View) {
        //producto = findViewById(R.id.titulo_producto)
    }

    private fun setupListeners(rootView: View) {

        verProducto(rootView)

    }

    private fun verProducto(rootView: View) {
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerCatalogo)
        recyclerView?.layoutManager = GridLayoutManager(requireActivity(),  2)

        // 2. Obtener la lista y convertirla a MutableList (NECESARIO para la eliminación local en el Adapter)
        val productosMutableList = ProductoDAO.obtenerTodosLosProductos().toMutableList()
        Log.d("Consola para ver todos la información aquí", "Tamaño de la lista de productos: ${productosMutableList.size}")

        // 3. Crear el Adapter, pasando AMBOS ARGUMENTOS REQUERIDOS:
        val adapter = ProductoCatalogoAdapter(productosMutableList, { productoClickeado ->

            val detallesFragmento = DetallesProductoFragment()
            val args = Bundle()
            args.putParcelable("PRODUCTO_SELECCIONADO", productoClickeado)

            detallesFragmento.arguments = args

            (requireActivity() as MainActivity).loadFragment(detallesFragmento)
        } )

        recyclerView?.adapter = adapter

        }

}