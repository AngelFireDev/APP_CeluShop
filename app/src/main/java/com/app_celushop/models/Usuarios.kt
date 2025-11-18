package com.app_celushop.models

data class Usuarios(
    val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val rol: String,
    val foto: String? = null
)
