package com.example.paymeapp

//CLASE DE DATOS PARA FIRESTORE
data class RegistroFirestore(
    val id: String = "",
    val fecha: String = "",
    val horasTrabajadas: Double = 0.0,
    val dineroGanado: Double = 0.0
)
