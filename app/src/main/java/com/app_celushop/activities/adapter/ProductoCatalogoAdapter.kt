package com.app_celushop.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.models.Producto

class ProductoCatalogoAdapter(private var productosList: MutableList<Producto>, private val onItemClickListener: (Producto) -> Unit): RecyclerView.Adapter<ProductoCatalogoHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductoCatalogoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductoCatalogoHolder(layoutInflater.inflate(R.layout.recycler_catalogo, parent, false))
    }

    override fun onBindViewHolder(
        holder: ProductoCatalogoHolder,
        position: Int
    ) {
        val item = productosList[position]
        holder.render(item, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return productosList.size

    }
}