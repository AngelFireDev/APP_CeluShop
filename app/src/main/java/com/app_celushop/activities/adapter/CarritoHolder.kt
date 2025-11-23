package com.app_celushop.activities.adapter

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.models.Carrito
import com.app_celushop.models.Producto
import com.bumptech.glide.Glide

class CarritoHolder(view: View, private val listener: CarritoAdapter.CarritoInteractionListener): RecyclerView.ViewHolder(view) {
    val nameProducto = view.findViewById<TextView>(R.id.product)

    val precio = view.findViewById<TextView>(R.id.price)
    val cantidad = view.findViewById<TextView>(R.id.amount)
    val image = view.findViewById<ImageView>(R.id.imageProducto)
    val btnAdd = view.findViewById<TextView>(R.id.btn_sumar_carrito)
    val btnReduce = view.findViewById<TextView>(R.id.btn_restar_carrito)
    val btnDelete = view.findViewById<ImageView>(R.id.Trash)

    fun render(itemCarrito: Carrito){
        val productoModel = itemCarrito.producto

        nameProducto.text = productoModel.nombre
        cantidad.text = itemCarrito.cantidad.toString()
        precio.text = productoModel.precio.toString()
        Glide.with(image.context).load(productoModel.url_imagen).into(image)

        // Lógica para Aumentar (+)
        btnAdd.setOnClickListener {
            val nuevaCantidad = itemCarrito.cantidad + 1
            listener.onCantidadChanged(itemCarrito, nuevaCantidad)
        }

        // Lógica para Disminuir (-)
        btnReduce.setOnClickListener {
            val nuevaCantidad = itemCarrito.cantidad - 1
            listener.onCantidadChanged(itemCarrito, nuevaCantidad)
        }

        btnDelete.setOnClickListener {
            // Llama a la función de eliminación en el Fragmento
            listener.onEliminarProducto(itemCarrito)
        }
    }

}