package com.example.paymeapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registro_jornada")
data class RegistroJornada(
    @PrimaryKey val dia: String, // Formato: YYYY-MM-DD
    val horasTrabajadas: Double,
    val sueldoGanado: Double
)
