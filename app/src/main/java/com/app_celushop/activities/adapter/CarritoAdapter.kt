package com.app_celushop.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.models.Producto
import com.app_celushop.models.Carrito

class CarritoAdapter(private var items: List<Carrito>,private val listener: CarritoInteractionListener) : RecyclerView.Adapter<CarritoHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarritoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_carrito, parent, false)
        return  CarritoHolder(view,listener)
    }

    override fun onBindViewHolder(holder: CarritoHolder, position: Int) {
        val itemCarrito = items[position]
        holder.render(itemCarrito)
    }

    override fun getItemCount(): Int {
        return items.size

    }

    interface CarritoInteractionListener {
        fun onCantidadChanged(item: Carrito, nuevaCantidad: Int)

        fun onEliminarProducto(item: Carrito)
    }
}