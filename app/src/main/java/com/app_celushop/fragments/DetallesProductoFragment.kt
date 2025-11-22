package com.app_celushop.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app_celushop.R
import com.app_celushop.activities.CarritoActivity
import com.app_celushop.activities.ConfirmacionCompraActivity
import com.app_celushop.models.Producto
import com.bumptech.glide.Glide

class DetallesProductoFragment: Fragment() {
    private lateinit var btn_carrito: Button
    private lateinit var btn_comprar: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usa el XML de tu activity_perfil (asumiendo que lo renombraste a fragment_perfil.xml)
        return inflater.inflate(R.layout.fragment_detalles_producto, container, false) // ⬅️ Usa el nombre de tu XML de perfil
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val producto: Producto? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Se pasa la clave (String) y la Clase (Producto::class.java)
            arguments?.getParcelable("PRODUCTO_SELECCIONADO", Producto::class.java)
        } else {
            // ➡️ API ANTIGUA (para compatibilidad con versiones anteriores a Android 13)
            @Suppress("DEPRECATION")
            arguments?.getParcelable("PRODUCTO_SELECCIONADO") as Producto?
        }

        // Usar los datos del producto
        if (producto != null) {
            val textNombre = view.findViewById<TextView>(R.id.tituloProducto)
            val textPrecio = view.findViewById<TextView>(R.id.precioProducto)
            val textMarca = view.findViewById<TextView>(R.id.ValorDatoMarca)
            val textModelo = view.findViewById<TextView>(R.id.ValorDatoModelo)
            val textAlmacenamiento = view.findViewById<TextView>(R.id.ValorDatoAlamacenamiento)
            val textRam = view.findViewById<TextView>(R.id.ValorDatoRam)
            val textColor = view.findViewById<TextView>(R.id.ValorDatoColor)
            val imageProducto = view.findViewById<ImageView>(R.id.imagenProducto)

            textNombre.text = producto.nombre
            textPrecio.text = "$${producto.precio}"
            textMarca.text = producto.marca
            textModelo.text = producto.modelo
            textAlmacenamiento.text = producto.almacenamiento
            textRam.text = producto.ram
            textColor.text = producto.color

            Glide.with(this).load(producto.url_imagen).into(imageProducto)
        }

        btn_carrito = view.findViewById(R.id.btn_carrito)
        btn_comprar = view.findViewById(R.id.btn_comprar)

        btn_carrito.setOnClickListener {
            val intent = Intent(requireContext(), CarritoActivity::class.java)
            startActivity(intent)
        }

        btn_comprar.setOnClickListener {
            val intent = Intent(requireContext(), ConfirmacionCompraActivity::class.java)
            startActivity(intent)
        }
    }
}