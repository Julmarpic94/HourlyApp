package com.example.paymeapp
import android.app.Application
import androidx.room.Room

//CLASE PARA INICIALIZAR LA BASE DE DATOS de ROOM (EN DESUSO
class MyApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Inicializar la base de datos
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "hourly_database"
        ).build()
    }
}