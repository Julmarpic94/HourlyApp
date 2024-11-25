package com.example.paymeapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [RegistroJornada::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun registroJornadaDao(): RegistroJornadaDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Maneja migraciones para evitar bloqueos
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
