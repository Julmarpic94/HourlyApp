package com.example.paymeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Estado para manejar la pantalla actual
            var currentView by remember { mutableStateOf("calendario") }
            var selectedDate by remember { mutableStateOf<String?>(null) }//Se iniciliza en nulo

            MaterialTheme {
                when (currentView) {
                    "calendario" -> CalendarioVista(
                        onDaySelected = { date ->
                            selectedDate = date
                            currentView = "registro_jornada"
                        },
                        onRegistroClick = {
                            currentView = "horas_registro" // Cambia a la vista de registros guardados
                        }
                    )
                    "registro_jornada" -> RegistroJornadaVista(
                        fecha = selectedDate!!,
                        onSave = {
                            currentView = "calendario" // Vuelve al calendario tras guardar
                        }
                    )
                    "horas_registro" -> HorasRegistro(
                        registros = registros, // Pasa la lista global de registros
                        onVolver = {
                            currentView = "calendario" // Vuelve al calendario
                        }
                    )
                }
            }
        }
    }
}