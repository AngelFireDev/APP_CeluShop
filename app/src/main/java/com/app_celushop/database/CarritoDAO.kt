package com.app_celushop.database

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.app_celushop.models.Carrito
import com.app_celushop.models.Producto

class CarritoDAO(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun añadirCarrito(producto: Producto): Boolean {
        //Abrir el modo escritura de la BD
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CARRITO_PRODUCTO_ID, producto.id_producto)
            put(DatabaseHelper.COLUMN_CARRITO_CANTIDAD, 1)
        }

        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_CARRITO} WHERE ${DatabaseHelper.COLUMN_CARRITO_PRODUCTO_ID}=?", arrayOf(producto.id_producto.toString()))
        if (cursor.count > 0) return false // Ya existe, no insertar
        cursor.close()

        //Insertar los valores en la tabla
        //Retorno -1 si no se pudo insertar
        val resultado = db.insert(DatabaseHelper.TABLE_CARRITO, null, values)
        return resultado != -1L
    }

    fun obtenerProductosEnCarrito(): MutableList<Carrito> {
        val listaItems = mutableListOf<Carrito>()
        val db = dbHelper.readableDatabase

        // Consulta SQL que une (JOIN) la tabla 'carrito' con la tabla 'productos'
        val query = """
    SELECT *
    FROM ${DatabaseHelper.TABLE_PRODUCTOS}      
    INNER JOIN ${DatabaseHelper.TABLE_CARRITO} 
    ON ${DatabaseHelper.COLUMN_CARRITO_PRODUCTO_ID} = ${DatabaseHelper.COLUMN_PRODUCTO_ID}
""".trimIndent()

        val cursor = db.rawQuery(query, null)
        Log.d(TAG, "Cursor count: ${cursor.count}")
        if (cursor.moveToFirst()) {
            do {
                val producto = Producto(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCTO_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_DESCRIPCION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_PRECIO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_URL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_MARCA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_MODELO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_ALMACENAMIENTO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_RAM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_COLOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUM_STOCK)),
                )

                val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CARRITO_CANTIDAD))

                listaItems.add(Carrito(producto, cantidad))

            } while (cursor.moveToNext())
        }

        cursor.close()
        return listaItems
    }

    fun eliminarProducto(productoId: Int): Int {
        val db = dbHelper.writableDatabase // Necesitas permisos de escritura

        val selection = "${DatabaseHelper.COLUMN_CARRITO_PRODUCTO_ID} = ?"
        val selectionArgs = arrayOf(productoId.toString())

        // Ejecuta la eliminación
        val deletedRows = db.delete(
            DatabaseHelper.TABLE_CARRITO,
            selection,
            selectionArgs
        )

        db.close()
        return deletedRows
    }

    fun actualizarCantidadProductoEnCarrito(idProducto: Int, nuevaCantidad: Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CARRITO_CANTIDAD, nuevaCantidad)
        }

        return db.update(
            DatabaseHelper.TABLE_CARRITO,
            values,
            "${DatabaseHelper.COLUMN_CARRITO_PRODUCTO_ID} = ?",
            arrayOf(idProducto.toString())
        )
    }

    fun calcularTotalProductos(): Double {
        val listaProductos = obtenerProductosEnCarrito()
        var subtotal = 0.0

        for (item in listaProductos) {
            subtotal += item.producto.precio.toDouble() * item.cantidad
        }
        return subtotal
    }

}