package com.app_celushop.database

import android.content.Context
import com.app_celushop.utils.PasswordHelper
import android.content.ContentValues
import android.provider.ContactsContract
import com.app_celushop.models.Usuarios


class UsuariosDAO(context: Context)  {
    //Instancia del DatabaseHelper para acceder a la BD
    private val dbHelper = DatabaseHelper(context)

    //Funcion de registro con return true / false
    fun registrarUsuario(usuarios: Usuarios): Boolean {
        //Abrir el modo escritura de la BD
        val db = dbHelper.writableDatabase
        //Encryptar la contraseña antes de que se guarde
        val passwordEncrypted = PasswordHelper.hashPassword(usuarios.password)
        //Contenedor Clave = Valor
        var values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NOMBRE, usuarios.nombre)
            put(DatabaseHelper.COLUMN_CORREO, usuarios.email)
            put(DatabaseHelper.COLUMN_CONTRASENA, passwordEncrypted)
            put(DatabaseHelper.COLUMN_ROL, usuarios.rol)
        }
        //Insertar los valores en la tabla
        // Retorna -1 si no inserta datos
        val resultado = db.insert(DatabaseHelper.TABLE_USUARIOS, null, values)
        db.close()
        return resultado != -1L
    }

    fun encriptarPassword(email: String): Boolean {
        val usuario = obtenerUsuario(email) ?: return false
        val passwordEncriptada = PasswordHelper.hashPassword(usuario.password)

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CONTRASENA, passwordEncriptada)
        }

        val resultado = db.update(
            DatabaseHelper.TABLE_USUARIOS,
            values,
            "${DatabaseHelper.COLUMN_CORREO} = ?",
            arrayOf(email)
        )
        db.close()
        return resultado > 0
    }

    //Actualizar foto
    fun actualizarFotoUsuario(correo: String, rutaFoto: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_FOTO, rutaFoto)
        }
        db.update("usuarios", values, "email = ?", arrayOf(correo))
        db.close()
    }

    //Validar Login
    fun validarLogin(email: String, password: String): Boolean {
        //Obtener usuario
        val usuario = obtenerUsuario(email)?: return false
        //Verificar si la clave es correcta
        val passwordEncrypted = PasswordHelper.hashPassword(password)
        return usuario.password == passwordEncrypted
    }

    fun obtenerUsuario(email: String): Usuarios? {
        var usuario: Usuarios? = null
        val db = dbHelper.readableDatabase

        //Generar el query
        val query = """
            SELECT * FROM ${DatabaseHelper.TABLE_USUARIOS}
            WHERE ${DatabaseHelper.COLUMN_CORREO} = ?
        """
        val cursor = db.rawQuery(query, arrayOf(email))

        //Recorrer los resultados de la consulta
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CORREO))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTRASENA))
            val rol = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROL))
            val foto = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO))

            usuario = Usuarios(id, nombre, email, password, rol, foto)
        }
        cursor.close()
        db.close()
        return usuario
    }

    //Actualizar Rol
    fun actualizarRol(email: String, nuevoRol: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ROL, nuevoRol)
        }
        val resultado = db.update(DatabaseHelper.TABLE_USUARIOS, values, "${DatabaseHelper.COLUMN_CORREO} = ?", arrayOf(email))
        db.close()
        return resultado > 0
    }

    //Validar el Email
    fun validarEmail(email: String): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_USUARIOS} WHERE ${DatabaseHelper.COLUMN_CORREO} = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }

    //Delete usuario
    fun deleteUsuario(email: String): Boolean {
        val db = dbHelper.writableDatabase
        val resultado = db.delete(DatabaseHelper.TABLE_USUARIOS, "${DatabaseHelper.COLUMN_CORREO} = ?", arrayOf(email))
        db.close()
        return resultado > 0
    }

    //Actualizar clave
    fun actualizarClave(email: String, nuevaClave: String): Boolean {
        val db = dbHelper.writableDatabase
        val passwordEncrypted = PasswordHelper.hashPassword(nuevaClave)
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CONTRASENA, passwordEncrypted)
        }
        val resultado = db.update(DatabaseHelper.TABLE_USUARIOS, values, "${DatabaseHelper.COLUMN_CORREO} = ?", arrayOf(email))
        db.close()
        return resultado > 0
    }

    //Actualizar Nombre
    fun actualizarUsuario(email: String, nuevoNombre: String, nuevaFoto: String?): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NOMBRE, nuevoNombre)
            put(DatabaseHelper.COLUMN_FOTO, nuevaFoto)
        }
        val resultado = db.update(
            DatabaseHelper.TABLE_USUARIOS,
            values,
            "${DatabaseHelper.COLUMN_CORREO} = ?",
            arrayOf(email)
        )
        db.close()
        return resultado > 0
    }
}
