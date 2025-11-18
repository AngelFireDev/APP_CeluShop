package com.app_celushop.activities.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.models.Producto
import com.bumptech.glide.Glide

class ProductoCatalogoHolder(view: View): RecyclerView.ViewHolder(view) {

    val nameProducto = view.findViewById<TextView>(R.id.titulo_producto)
    val descripcion = view.findViewById<TextView>(R.id.descripcionProducto)
    val precio = view.findViewById<TextView>(R.id.precioProducto)
    val image = view.findViewById<ImageView>(R.id.imagenProducto)

    fun render(productoModel: Producto, onItemClick: (Producto) -> Unit){
        nameProducto.text = productoModel.nombre
        descripcion.text = productoModel.descripcion
        precio.text = productoModel.precio.toString()
        Glide.with(image.context).load(productoModel.url_imagen).into(image)

        itemView.setOnClickListener {
            onItemClick(productoModel) // Envía el objeto Producto completo
        }
    }
}