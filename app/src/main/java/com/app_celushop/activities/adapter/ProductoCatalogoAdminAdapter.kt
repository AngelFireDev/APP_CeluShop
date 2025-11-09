package com.app_celushop.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app_celushop.R
import com.app_celushop.models.Producto

class ProductoCatalogoAdminAdapter(private var productosList: List<Producto>): RecyclerView.Adapter<ProductoCatalogoAdminHolder>() {
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
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return productosList.size

    }
}