package com.app_celushop.models

data class Producto(
    var id_producto: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Int = 0,
    val url_imagen: String,
    val marca: String,
    val modelo: String,
    val almacenamiento: String,
    val ram: String,
    val color: String,
    val stock: Int = 0,
)
