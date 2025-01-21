package com.example.paymeapp


import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//CLASE CONTROLADOR CON TODOS LOS METODOS PARA GESTIONAR LOS REGISTROS

// Lista mutable para almacenar los registros

// Función para calcular las horas trabajadas
fun calcularHorasTrabajadas(horaEntrada: String, horaSalida: String): Double {
    return try {
        val formato = SimpleDateFormat("HH:mm", Locale.FRANCE)
        val entrada = formato.parse(horaEntrada)
            ?: throw IllegalArgumentException("Hora de entrada no válida")
        var salida =
            formato.parse(horaSalida) ?: throw IllegalArgumentException("Hora de salida no válida")

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

// FIRESTORE

fun guardarFirestore(
    registro: RegistroFirestore
) {
    val baseDatos = FirebaseFirestore.getInstance()

    baseDatos.collection("registros")
        .add(registro)// agrego el objeto de registo
}

suspend fun obtenerRegistrosFirestore(): List<RegistroFirestore> {
    val registros = mutableListOf<RegistroFirestore>()
    val baseDatos = FirebaseFirestore.getInstance()

    try {
        val snapshot = baseDatos.collection("registros").get().await()
        snapshot.documents.forEach { doc ->
            val registro = doc.toObject(RegistroFirestore::class.java)
            if (registro != null) {
                registros.add(registro.copy(id = doc.id))// pasamos el id generado al docuemento
            }
        }
    } catch (e: Exception) {
        println("Error al cargar Registros: ${e.message}")
    }
    return registros
}

fun borrarRegistroDB(
    registroId: String
){
    val baseDatos = FirebaseFirestore.getInstance()
    baseDatos.collection("registros").document(registroId)
        .delete()
        .addOnSuccessListener {
            println("Registro con ID $registroId borrado exitosamente")
        }
        .addOnFailureListener { exception ->
            println("Error al borrar registro: ${exception.message}")
        }
}

fun limpiarRegistrosDB(){
    val baseDatos = FirebaseFirestore.getInstance()
    baseDatos.collection("registros")
        .get()
        .addOnSuccessListener { snapshot ->
            val batch = baseDatos.batch()
            snapshot.documents.forEach{ doc ->
                batch.delete(doc.reference)
            }
            batch.commit()
                .addOnFailureListener { exception ->
                    println("Error al borrar registro: ${exception.message}")
                }
        }
}



