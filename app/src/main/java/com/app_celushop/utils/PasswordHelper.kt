package com.app_celushop.utils

import java.security.MessageDigest

object PasswordHelper {
    fun hashPassword(password:String): String {
        //Convertir String a bytes
        val bytes = password.toByteArray()
        val md= MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold(""){str,it -> str + "%02x".format(it)}
    }
    
    //Validacion de las contrasenas
    fun verificarPassword(password: String, passwordHashed:String): Boolean {
        return hashPassword(password) == passwordHashed
    }
}