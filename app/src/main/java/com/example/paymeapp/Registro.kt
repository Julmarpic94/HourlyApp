package com.example.paymeapp

import androidx.room.Entity
import androidx.room. PrimaryKey

//CLASE PARA CREAR LA ENTIDAD DE REGISTRO
@Entity(tableName = "registros")
data class Registro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: String,
    val horasTrabajadas: Double,
    val dineroGanado:Double
)
