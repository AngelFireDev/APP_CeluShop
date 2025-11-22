package com.app_celushop.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app_celushop.R
import com.app_celushop.activities.CatalogoAdministradorActivity
import com.app_celushop.activities.MainActivity
import com.app_celushop.database.ProductoDAO
import com.app_celushop.database.UsuariosDAO
import com.app_celushop.models.Producto

class AnadirProductoFragment: Fragment() {
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
    private lateinit var ProductoDAO: ProductoDAO

    private var fragmentRootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usa el XML de tu activity_perfil (asumiendo que lo renombraste a fragment_perfil.xml)
        return inflater.inflate(R.layout.fragment_anadir_producto, container, false) // ⬅️ Usa el nombre de tu XML de perfil
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializar DAO
        ProductoDAO = ProductoDAO(requireContext())
        fragmentRootView = view
        initViews(view)
        setupListeners()

    }

    private fun setupListeners() {
        btnAdd.setOnClickListener {
            registrarProducto()
        }

        recibirDatosProducto()

        btnCancel.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun recibirDatosProducto() {
        productoAEditar = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 (API 33) y superior
            arguments?.getParcelable("PRODUCTO_A_EDITAR", Producto::class.java)
        } else {
            // Para versiones anteriores
            @Suppress("DEPRECATION")
            arguments?.getParcelable("PRODUCTO_A_EDITAR")
        }


        if (productoAEditar != null) {
            // ... (Tu lógica para rellenar campos) ...
            rellenarCampos(productoAEditar!!)
            // ...
        }
    }

    private fun rellenarCampos(producto: Producto) {
        val rootView = fragmentRootView ?: return
        // Rellenar todos los campos del formulario
        rootView.findViewById<EditText>(R.id.productName).setText(producto.nombre)
        rootView.findViewById<EditText>(R.id.productDescription).setText(producto.descripcion)
        rootView.findViewById<EditText>(R.id.productPrice).setText(producto.precio.toString())
        rootView.findViewById<EditText>(R.id.productImage).setText(producto.url_imagen)
        rootView.findViewById<EditText>(R.id.productBrand).setText(producto.marca)
        rootView.findViewById<EditText>(R.id.productModel).setText(producto.modelo)
        rootView.findViewById<EditText>(R.id.productStorage).setText(producto.almacenamiento)
        rootView.findViewById<EditText>(R.id.productRam).setText(producto.ram)
        rootView.findViewById<EditText>(R.id.productColor).setText(producto.color)
        rootView.findViewById<EditText>(R.id.productStock).setText(producto.stock.toString())

        // Nota: Si tienes campos de imagen, también debes cargarlos aquí (ej. usando Glide/Picasso)
    }

    private fun initViews(view: View) {
        et_producto = view.findViewById(R.id.productName)
        et_descripción = view.findViewById(R.id.productDescription)
        et_precio = view.findViewById(R.id.productPrice)
        et_imagen = view.findViewById(R.id.productImage)
        et_marca = view.findViewById(R.id.productBrand)
        et_modelo = view.findViewById(R.id.productModel)
        et_almacenamiento = view.findViewById(R.id.productStorage)
        et_ram = view.findViewById(R.id.productRam)
        et_color = view.findViewById(R.id.productColor)
        et_stock = view.findViewById(R.id.productStock)
        btnAdd = view.findViewById(R.id.btn_save)
        btnCancel = view.findViewById(R.id.btn_cancel)
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
                Toast.makeText(requireContext(), "Por favor completa los campos", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "Producto actualizado exitosamente.", Toast.LENGTH_SHORT).show()
                requireActivity().setResult(Activity.RESULT_OK)
                requireActivity().finish() // Cierra la Activity y regresa al catálogo
            } else {
                Toast.makeText(requireContext(), "Error al actualizar el producto.", Toast.LENGTH_SHORT).show()
            }
        }else{
            val agregado = ProductoDAO.añadirProducto(producto)

            if(agregado) {
                Toast.makeText(requireContext(), "Producto registrado exitosamente", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).loadFragment(CatalogoAdministradorFragment())

            }else {
                Toast.makeText(requireContext(), "Error al registrar producto", Toast.LENGTH_SHORT).show()
            }
        }
    }

}