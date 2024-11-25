package com.example.paymeapp

import java.text.SimpleDateFormat
import java.util.*

object RegistroJornadaControlador {

    fun calcularHorasTrabajadas(
        horaEntrada: String,
        horaSalida: String,
        precioHora: String
    ): Pair<Double, Double>{
        return try {
            val formato = SimpleDateFormat("HH:mm", Locale.FRANCE)
            val entrada = formato.parse(horaEntrada) ?: throw IllegalArgumentException("Hora de entrada no válida")
            var salida = formato.parse(horaSalida) ?: throw IllegalArgumentException("Hora de Salida no válida")

            if (salida.before(entrada)) {
                salida = Date(salida.time + 24 * 60 * 60 * 1000)
            }

            val diferencia = salida.time - entrada.time
            val horas = diferencia / (1000 * 60 * 60)
            val minutos = (diferencia / (1000 * 60)) % 60

            //Convertir prceio por hora
            val precio = when (precioHora) {
                "Día natural 6.20€/h" -> 6.2
                "Noche natural 8€/h" -> 8.0
                "Día festivo/Finde 8€/h" -> 8.0
                "Noche festivo/Finde 10€/h" -> 10.0
                else -> throw IllegalArgumentException("Hora de Salida no válida")
            }

            val horasDecimal = horas + minutos / 60.0
            val total = horasDecimal * precio

            Pair(horasDecimal, total)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(0.0, 0.0)
        }
    }
}
