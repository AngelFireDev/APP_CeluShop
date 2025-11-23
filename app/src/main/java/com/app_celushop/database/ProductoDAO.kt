package com.app_celushop.database

import android.content.ContentValues
import android.content.Context
import com.app_celushop.models.Producto

class ProductoDAO(context: Context) {
    //Instancia del DatabaseHelper para acceder a la BD
    private val dbHelper = DatabaseHelper(context)

    //Funcion de ingreso de productos
    fun añadirProducto(producto: Producto): Boolean {
        //Abrir el modo escritura de la BD
        val db = dbHelper.writableDatabase
        //Contenedor clave - valor
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUM_NOMBRE, producto.nombre)
            put(DatabaseHelper.COLUM_DESCRIPCION, producto.descripcion)
            put(DatabaseHelper.COLUM_PRECIO, producto.precio)
            put(DatabaseHelper.COLUM_URL, producto.url_imagen)
            put(DatabaseHelper.COLUM_MARCA, producto.marca)
            put(DatabaseHelper.COLUM_MODELO, producto.modelo)
            put(DatabaseHelper.COLUM_ALMACENAMIENTO, producto.almacenamiento)
            put(DatabaseHelper.COLUM_RAM, producto.ram)
            put(DatabaseHelper.COLUM_COLOR, producto.color)
            put(DatabaseHelper.COLUM_STOCK, producto.stock)

        }
        //Insertar los valores en la tabla
        //Retorno -1 si no se pudo insertar
        val resultado = db.insert(DatabaseHelper.TABLE_PRODUCTOS, null, values)
        return resultado != -1L
    }

    //Obtener todos los productos
    fun obtenerTodosLosProductos(): MutableList<Producto> {
        val listaProductos = mutableListOf<Producto>()
        val db = dbHelper.readableDatabase

        //Generar la consulta BD (Query)
        val query = """
            SELECT * FROM ${DatabaseHelper.TABLE_PRODUCTOS} 
        """
        val cursor = db.rawQuery(query, null)

        //Recorre los resultados de la consulta
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCTO_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_NOMBRE))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_DESCRIPCION))
            val precio = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_PRECIO))
            val url = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_URL))
            val marca = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_MARCA))
            val modelo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_MODELO))
            val almacenamiento = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_ALMACENAMIENTO))
            val ram = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_RAM))
            val color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_COLOR))
            val stock = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_STOCK))
            val producto = Producto(id,nombre,descripcion,precio,url,marca,modelo,almacenamiento,ram,color,stock)
            listaProductos.add(producto)
        }
        cursor.close()
        return listaProductos
    }

    //Obtener el producto
    fun obtenerProducto(nombre: String): Producto? {
        var producto: Producto? = null
        val db = dbHelper.readableDatabase

        //Generar la consulta BD (Query)
        val query = """
            SELECT * FROM ${DatabaseHelper.TABLE_PRODUCTOS} WHERE ${DatabaseHelper.COLUM_NOMBRE} = ?
        """
        val cursor = db.rawQuery(query, arrayOf(nombre))

        //Recorre los resultados de la consulta
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCTO_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_NOMBRE))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_DESCRIPCION))
            val precio = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_PRECIO))
            val url = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_URL))
            val marca = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_MARCA))
            val modelo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_MODELO))
            val almacenamiento = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_ALMACENAMIENTO))
            val ram = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_RAM))
            val color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_COLOR))
            val stock = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_STOCK))
        producto = Producto(id,nombre,descripcion,precio,url,marca,modelo,almacenamiento,ram,color,stock)
        }
        cursor.close()
        db.close()
        return producto
    }

    //Validar el Producto
    fun validarProducto(email: String): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_PRODUCTOS} WHERE ${DatabaseHelper.COLUM_NOMBRE} = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }

    //Actualizar Descripcion
    fun actualizarProducto(producto: Producto): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            // Agrega todos los campos que deseas actualizar
            put(DatabaseHelper.COLUM_NOMBRE, producto.nombre)
            put(DatabaseHelper.COLUM_DESCRIPCION, producto.descripcion)
            put(DatabaseHelper.COLUM_PRECIO, producto.precio)
            put(DatabaseHelper.COLUM_URL, producto.url_imagen)
            put(DatabaseHelper.COLUM_MARCA, producto.marca)
            put(DatabaseHelper.COLUM_MODELO, producto.modelo)
            put(DatabaseHelper.COLUM_ALMACENAMIENTO, producto.almacenamiento)
            put(DatabaseHelper.COLUM_RAM, producto.ram)
            put(DatabaseHelper.COLUM_COLOR, producto.color)
            put(DatabaseHelper.COLUM_STOCK, producto.stock)
        }

        // Definir la cláusula WHERE: SOLO actualizar la fila con el ID coincidente
        val selection = "${DatabaseHelper.COLUMN_PRODUCTO_ID} = ?"
        val selectionArgs = arrayOf(producto.id_producto.toString())

        // Ejecutar el UPDATE
        val resultado = db.update(
            DatabaseHelper.TABLE_PRODUCTOS,
            values,
            selection,
            selectionArgs
        )

        db.close()

        // Si resultado > 0, significa que al menos una fila fue actualizada
        return resultado > 0
    }


    //Eliminar producto
    fun eliminarProducto(id: String): Boolean{
        val db = dbHelper.writableDatabase
        val resultado = db.delete(DatabaseHelper.TABLE_PRODUCTOS, "${DatabaseHelper.COLUMN_PRODUCTO_ID}=?",
            arrayOf(id))
        db.close()
        return resultado > 0
    }
}