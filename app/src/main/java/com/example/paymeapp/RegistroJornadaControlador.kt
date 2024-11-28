package com.example.paymeapp

import java.text.SimpleDateFormat
import java.util.*

// Lista mutable para almacenar los registros
val registros = mutableListOf<String>()

// Función para calcular las horas trabajadas
fun calcularHorasTrabajadas(horaEntrada: String, horaSalida: String): Double {
    return try {
        val formato = SimpleDateFormat("HH:mm", Locale.FRANCE)
        val entrada = formato.parse(horaEntrada) ?: throw IllegalArgumentException("Hora de entrada no válida")
        var salida = formato.parse(horaSalida) ?: throw IllegalArgumentException("Hora de salida no válida")

        if (salida.before(entrada)) {
            // Sumar 24 horas si la salida es el día siguiente
            salida = Date(salida.time + 24 * 60 * 60 * 1000)
        }

        val diferencia = salida.time - entrada.time
        val horas = diferencia / (1000 * 60 * 60)
        val minutos = (diferencia / (1000 * 60)) % 60

        // Convertir las horas y minutos a formato decimal
        horas + minutos / 60.0
    } catch (e: Exception) {
        e.printStackTrace()
        0.0
    }
}

// Función para calcular el dinero ganado
fun calcularDineroGanado(horasTrabajadas: Double, precioHora: String): Double {
    return try {
        val precio = when (precioHora) {
            "Día natural 6.20€/h" -> 6.2
            "Noche natural 8€/h" -> 8.0
            "Día festivo/Finde 8€/h" -> 8.0
            "Noche festivo/Finde 10€/h" -> 10.0
            else -> throw IllegalArgumentException("Precio por hora no válido")
        }

        horasTrabajadas * precio
    } catch (e: Exception) {
        e.printStackTrace()
        0.0
    }
}

// Función para calcular el registro completo
fun calcularRegistroCompleto(
    fecha: String,
    horaEntrada: String,
    horaSalida: String,
    precioHora: String
): String {
    return try {
        val horasTrabajadas = calcularHorasTrabajadas(horaEntrada, horaSalida)
        val sueldoGanado = calcularDineroGanado(horasTrabajadas, precioHora)

        // Crear el registro como texto
        "Fecha: $fecha\nHoras trabajadas: $horasTrabajadas\nDinero ganado: $sueldoGanado €"
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

// Función para guardar un registro en la lista
fun guardarRegistro(registro: String) {
    if (registro.isNotEmpty() && !registro.startsWith("Error")) {
        registros.add(registro)
    } else {
        println("Registro inválido. Asegúrate de calcular antes de guardar.")
    }
}

// Función para mostrar todos los registros almacenados
fun mostrarRegistros(): List<String> {
    return registros
}
