package com.example.paymeapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//CLASE PARA CREAR LA BASE DE DATOS CON ROOM

@Database(entities = [Registro::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registroDao(): RegistroDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        //FUNCION PARA OBTENER LA BASE DE DATOS Y DARLE UN CONTEXTO, evita dar cotexto cada vez que la llamemos
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hourly_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
