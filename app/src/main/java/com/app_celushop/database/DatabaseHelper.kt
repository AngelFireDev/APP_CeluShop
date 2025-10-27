package com.app_celushop.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        companion object{
            private const val DATABASE_NAME = "CeluShopDB"
            private const val DATABASE_VERSION = 1

            const val TABLE_PRODUCTOS = "productos"
            const val COLUM_ID = "id_producto"
            const val COLUM_NOMBRE = "nombre"
            const val COLUM_DESCRIPCION = "descripcion"
            const val COLUM_PRECIO = "precio"
            const val COLUM_URL = "url_imagen"
            const val COLUM_MARCA = "marca"
            const val COLUM_MODELO = "modelo"
            const val COLUM_ALMACENAMIENTO = "almacenamiento"
            const val COLUM_RAM = "ram"
            const val COLUM_COLOR = "color"
            const val COLUM_STOCK = "stock"

            private const val CREATE_TABLE = """
                CREATE TABLE $TABLE_PRODUCTOS(
                    $COLUM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUM_NOMBRE TEXT NOT NULL,
                    $COLUM_DESCRIPCION TEXT NOT NULL,
                    $COLUM_PRECIO INTEGER NOT NULL,
                    $COLUM_URL TEXT NOT NULL,
                    $COLUM_MARCA TEXT NOT NULL,
                    $COLUM_MODELO TEXT NOT NULL,
                    $COLUM_ALMACENAMIENTO TEXT NOT NULL,
                    $COLUM_RAM TEXT NOT NULL,
                    $COLUM_COLOR TEXT NOT NULL,
                    $COLUM_STOCK INTEGER NOT NULL
                )"""
        }
            //Ejecutar por primera vez la BD y que lo haga solo una vez
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL(CREATE_TABLE)
            }

            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                db.execSQL(CREATE_TABLE)
            }
        }
