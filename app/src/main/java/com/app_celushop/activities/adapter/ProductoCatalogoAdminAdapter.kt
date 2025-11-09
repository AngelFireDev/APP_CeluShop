package com.app_celushop.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.models.Producto

class ProductoCatalogoAdminAdapter(private var productosList: MutableList<Producto>, private val onDeleteClickListener: (Int) -> Unit,private val onEditClickListener: (Producto) -> Unit): RecyclerView.Adapter<ProductoCatalogoAdminHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductoCatalogoAdminHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductoCatalogoAdminHolder(layoutInflater.inflate(R.layout.recycler_catalogo_administrador, parent, false))
    }

    override fun onBindViewHolder(
        holder: ProductoCatalogoAdminHolder,
        position: Int
    ) {
        val item = productosList[position]
        holder.render(productoModel = item,
            onDeleteClick = { idProducto ->
                // Cuando el Holder hace clic, ejecuta el callback de la Activity
                onDeleteClickListener(idProducto)

                // Actualiza la lista del Adapter para refrescar la vista
                productosList.removeAt(position)
                notifyItemRemoved(position)
            },onEditClick = { productoAEditar ->
                onEditClickListener(productoAEditar)
            })
    }

    override fun getItemCount(): Int {
        return productosList.size

    }
}