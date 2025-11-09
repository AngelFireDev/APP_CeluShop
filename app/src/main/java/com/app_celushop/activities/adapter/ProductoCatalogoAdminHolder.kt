package com.app_celushop.activities.adapter

import android.media.Image
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.models.Producto
import com.bumptech.glide.Glide

class ProductoCatalogoAdminHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameProducto = view.findViewById<TextView>(R.id.ValorDatoNombre)
    val descripcion = view.findViewById<TextView>(R.id.ValorDatoPrecio)
    val precio = view.findViewById<TextView>(R.id.ValorDatoDescripcion)
    val marca = view.findViewById<TextView>(R.id.ValorDatoMarca)
    val modelo = view.findViewById<TextView>(R.id.ValorDatoModelo)
    val almacenamiento = view.findViewById<TextView>(R.id.ValorDatoAlamacenamiento)
    val ram = view.findViewById<TextView>(R.id.ValorDatoRam)
    val color = view.findViewById<TextView>(R.id.ValorDatoColor)
    val stock = view.findViewById<TextView>(R.id.ValorDatoStock)
    val image = view.findViewById<ImageView>(R.id.imageProducto)

    val btnEdit = view.findViewById<ImageView>(R.id.edit)
    val btnDelete = view.findViewById<ImageView>(R.id.delete)

    fun render(productoModel: Producto, onDeleteClick: (Int) -> Unit, onEditClick: (Producto) -> Unit){
        nameProducto.text = productoModel.nombre
        descripcion.text = productoModel.descripcion
        precio.text = productoModel.precio.toString()
        marca.text = productoModel.marca
        modelo.text = productoModel.modelo
        almacenamiento.text = productoModel.almacenamiento
        ram.text = productoModel.ram
        color.text = productoModel.color
        stock.text = productoModel.stock.toString()
        Glide.with(image.context).load(productoModel.url_imagen).into(image)
        btnEdit.setOnClickListener { onEditClick(productoModel) }
        btnDelete.setOnClickListener { onDeleteClick(productoModel.id_producto) }
    }
}