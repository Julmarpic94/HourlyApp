package com.example.paymeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paymeapp.ui.theme.AzulClaro
import com.example.paymeapp.ui.theme.AzulOscuro

//PANTALLA VISTA PARA MOSTRAR LA LISTA DE REGISTROS

@Composable
fun HorasRegistrosVista(
    onVolver: () -> Unit
) {
    val registros = remember { mutableStateListOf<RegistroFirestore>() }
    // Función secundaria para completar la lista d elos registros, obtenidos de FireStore
    LaunchedEffect(Unit) {
        registros.clear()
        registros.addAll(obtenerRegistrosFirestore())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //If para mostrar la pantalla vacia con la opcion de volver
        if (registros.isEmpty()) {
            Text(
                text = "No hay registros disponibles.",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            //Si hay registros los muesta y crea la lista
        } else {
            HorasRegistro(
                registros = registros,
                onVolver = onVolver
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para volver
        Button(
            onClick = { onVolver() },
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulOscuro,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Volver", style = TextStyle(fontSize = 16.sp))
        }
    }
}

@Composable
fun HorasRegistro(
    //Lista mutable para poder crear la accion de borrar
    registros: MutableList<RegistroFirestore>,
    onVolver: () -> Unit
) {
    var totalTexto: String by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registros",
            style = TextStyle(
                color = AzulOscuro,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            //Mostramos cada registro con un Item e iteramos sobre la lista de regsitros

            registros.forEach { registro ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ícono al principio
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_day),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)

                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Texto del Registros
                    Text(
                        text = "Fecha: ${registro.fecha}\n" +
                                "Horas: ${registro.horasTrabajadas}\n" +
                                "Ganado: ${registro.dineroGanado}€",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    //Boton registro para borrar
                    Button(
                        onClick = {
                            borrarRegistroDB(registro.id)
                            registros.removeIf { it.id == registro.id }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulClaro,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Borrar", style = TextStyle(fontSize = 14.sp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para volver
        Button(
            onClick = { onVolver() },
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulOscuro,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Volver", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                limpiarRegistrosDB() // elimino todo
                registros.clear()

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulClaro,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Limpiar", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val (totalRegistros, totalHoras, totalDinero) = calcularTotales(registros)
                totalTexto = "Total de registros: $totalRegistros\n" +
                        "Total de horas trabajadas: ${"%.2f".format(totalHoras)}\n" +
                        "Total de dinero ganado: ${"%.2f".format(totalDinero)} €"
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulOscuro,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Total", style = TextStyle(fontSize = 16.sp))
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (totalTexto.isNotEmpty()){
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = totalTexto,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = AzulOscuro,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

//Funcion para calcular totales con un return triple
fun calcularTotales(registros: List<RegistroFirestore>): Triple<Int, Double, Double> {
    val totalRegistros = registros.size
    val totalHoras = registros.sumOf { it.horasTrabajadas }
    val totalDinero = registros.sumOf { it.dineroGanado }
    return Triple(totalRegistros, totalHoras, totalDinero)

}


//vista previa
@Preview(showBackground = true, widthDp = 400, heightDp = 600)
@Composable
fun PreviewHorasRegistro() {
    // Lista de ejemplo para la vista previa
    val registrosEjemplo = mutableListOf(
        RegistroFirestore(fecha = "2024-11-26", horasTrabajadas = 8.5, dineroGanado = 52.7),
        RegistroFirestore(fecha = "2024-11-27", horasTrabajadas = 6.0, dineroGanado = 37.2)
    )

    // Llamada a la función HorasRegistro con datos de ejemplo
    HorasRegistro(
        registros = registrosEjemplo,
        onVolver = { println("Volver al calendario (simulado)") }
    )
}
