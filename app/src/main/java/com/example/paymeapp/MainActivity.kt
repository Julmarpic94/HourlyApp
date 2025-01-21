package com.example.paymeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

//PRINCIPAL ENCARGADA DE GESTIONAR LA NAVEGACION ENTRE PANTALLAS
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Estado para manejar la pantalla actual
            var vistaPrincipal by remember { mutableStateOf("login") }

            //Variable para manejar las fechas, se inicializa en nulo para que se acceda al calendario, si no es nulo cambi la pantalla
            var fechaSeleccionada by remember { mutableStateOf<String?>(null) }

            //inicializamos los registros( los registros String)
            var registros by remember { mutableStateOf(listOf<RegistroFirestore>()) }

            //corrutina para cargar datos de la base de datos
            val corrutina = rememberCoroutineScope()

            //Switch encargado de gestionar las pantallas, según el String de vistaprincipal
            MaterialTheme {
                when (vistaPrincipal) {
                    "login" -> LoginVista(
                        onLoginSuccess = {
                            vistaPrincipal = "calendario"// pone vista del calendario
                        }
                    )

                    "calendario" -> CalendarioVista(
                        onDaySelected = { fecha ->
                            fechaSeleccionada = fecha
                            vistaPrincipal = "registro_jornada"
                        },
                        onRegistroClick = {
                            //Lanzamos una corrutina para cargar los registros
                                corrutina.launch {
                                    registros = obtenerRegistrosFirestore()
                                }

                            vistaPrincipal = "horas_registro" // Cambia a la vista de registros guardados
                        }
                    )
                    "registro_jornada" -> RegistroJornadaVista(
                        fecha = fechaSeleccionada!!,
                        guardar = {
                            vistaPrincipal = "calendario" // Vuelve al calendario tras guardar
                        }
                    )
                    "horas_registro" -> HorasRegistrosVista(
                        onVolver = {
                            vistaPrincipal = "calendario"
                        }
                    )
                }
            }
        }
    }
}