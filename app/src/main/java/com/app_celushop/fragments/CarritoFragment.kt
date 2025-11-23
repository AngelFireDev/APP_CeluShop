package com.app_celushop.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.activities.ConfirmacionCompraActivity
import com.app_celushop.activities.MainActivity
import com.app_celushop.activities.adapter.CarritoAdapter
import com.app_celushop.database.CarritoDAO
import com.app_celushop.database.DatabaseHelper
import com.app_celushop.models.Carrito
import java.text.NumberFormat
import java.util.Locale

class CarritoFragment: Fragment(), CarritoAdapter.CarritoInteractionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carritoAdapter: CarritoAdapter
    private lateinit var carritoDAO: CarritoDAO
    private var listaDeItems: MutableList<Carrito> = mutableListOf()

    private lateinit var textViewTotal: TextView
    private lateinit var btn_comprar: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_carrito, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        carritoDAO = CarritoDAO(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_comprar = view.findViewById(R.id.btnContinue)
        recyclerView = view.findViewById(R.id.recyclerCarrito)
        carritoAdapter = CarritoAdapter(listaDeItems, this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carritoAdapter
        }

        textViewTotal = view.findViewById(R.id.priceShoppingCart)

        cargarDatosDelCarrito()

        btn_comprar.setOnClickListener {
            val totalActual = carritoDAO.calcularTotalProductos()

            if (totalActual > 0.0) {
                (requireActivity() as MainActivity).loadFragment(ConfirmacionCompraFragment())
            } else {
                Toast.makeText(
                    requireContext(),
                    "El carrito está vacío. Agregue productos para continuar.",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarDatosDelCarrito() {
        val nuevosItems = carritoDAO.obtenerProductosEnCarrito()

        listaDeItems.clear()
        listaDeItems.addAll(nuevosItems)

        var totalGlobal = 0.0

        for (item in nuevosItems) {
            val subtotal = item.producto.precio.toDouble() * item.cantidad
            totalGlobal += subtotal
        }

        actualizarVistaTotal(totalGlobal)

        if (::carritoAdapter.isInitialized) {
            carritoAdapter.notifyDataSetChanged()
        }
    }


    override fun onResume() {
        super.onResume()
        cargarDatosDelCarrito()
    }



    override fun onCantidadChanged(item: Carrito, nuevaCantidad: Int) {
        if (nuevaCantidad > 0) {
            carritoDAO.actualizarCantidadProductoEnCarrito(
                item.producto.id_producto,
                nuevaCantidad
            )
            cargarDatosDelCarrito()
        } else {
            onEliminarProducto(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onEliminarProducto(item: Carrito) {
        val deletedRows = carritoDAO.eliminarProducto(item.producto.id_producto)

        if (deletedRows > 0) {
            listaDeItems.remove(item)

            carritoAdapter.notifyDataSetChanged()

            cargarDatosDelCarrito()

            Toast.makeText(requireContext(), "Producto ${item.producto.nombre} eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarVistaTotal(total: Double) {
        // Formatear el número como moneda (ejemplo para pesos colombianos/latinoamericanos)
        @Suppress("DEPRECATION") val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CO")) // O su Locale local
        formatoMoneda.maximumFractionDigits = 0 // Quitar decimales si el precio es entero

        val totalFormateado = formatoMoneda.format(total)

        // Asignar el valor a la vista
        textViewTotal.text = totalFormateado
    }
}