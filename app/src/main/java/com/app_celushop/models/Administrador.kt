package com.app_celushop.models

data class Administrador(
    var documento: String,
    val tipo_documento: String,
    val nombre: String,
    val correo: String,
    val contraseña: String,
)
