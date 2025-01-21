package com.example.paymeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paymeapp.ui.theme.AzulOscuro
import com.example.paymeapp.ui.theme.BlancoAzul
import java.util.Calendar


//VISTA PRINCIPAL DEL CALENDARIO
@Composable
fun CalendarioVista(
    onDaySelected: (String) -> Unit, //funcion para seleccionar dia y abrir Registro
    onRegistroClick: () -> Unit // funcion para volver al pulsar en VerRegistros

) {
    val scrollState = rememberScrollState() // Barra para permitir girar y hacer scroll en la vista

    // Información del calendario
    val calendario = Calendar.getInstance() //Obtenemos el calendario de hoy, dia, mes y ano
    val diasMes =
        remember { mutableStateListOf<Int>() }//Creamos la variable para listas los dias del mes
    calendario.set(Calendar.DAY_OF_MONTH, 1)
    val totalDias =
        calendario.getActualMaximum(Calendar.DAY_OF_MONTH)//calculamos el total de dias con del mes, segun nuesto calendario

    // Genera los días del mes
    LaunchedEffect(Unit) {//Activamos una funcion secundaria que gener lo dias del mes
        diasMes.clear()
        diasMes.addAll(1..totalDias)
    }


        // Contenedor principal con desplazamiento
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White.copy(alpha = 0.8f))
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texto "Calendario"
            Text(
                text = "Calendario",
                style = TextStyle(
                    color = AzulOscuro,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Calendario con los días del mes
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = obtenerMesyAnoString(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 25.dp)
                )
                //Creamos un grid vertical de 7 columnas representando el calendario
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // Altura fija para el calendario
                ) {
                    items(diasMes) { day ->
                        val fechaSeleccionada =
                            "${calendario.get(Calendar.YEAR)}-${calendario.get(Calendar.MONTH) + 1}-$day"
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .background(
                                    color = BlancoAzul,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .aspectRatio(1f)
                                .clickable { onDaySelected(fechaSeleccionada) }, // Detecta el click en el día
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "$day",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                //Boton para ver los registros, nos envia ala pantalla registros
                Button(
                    onClick = { onRegistroClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulOscuro,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(48.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(text = " Ver Registro", style = TextStyle(fontSize = 16.sp))
                }
            }
        }
    }


//Funcion para implementar el nombre del mes e imprime un icono representativo de unicode
fun obtenerMesyAnoString(): String {
    val calendario = Calendar.getInstance()
    val ano = calendario.get(Calendar.YEAR)
    val mes = calendario.get(Calendar.MONTH)
    val mesString = when (mes + 1) {
        1 -> "\u2744 Enero"
        2 -> "Febrero \uD83C\uDF42"
        3 -> "Marzo \uD83C\uDF38"
        4 -> "Abril \uD83C\uDF27"
        5 -> "\uD83C\uDF3C Mayo"
        6 -> "\uD83C\uDF34 Junio"
        7 -> "\uD83C\uDFD6 Julio"
        8 -> "\u2600 Agosto"
        9 -> "\uD83C\uDF41 Septiembre"
        10 -> "\uD83C\uDF83 Octubre"
        11 -> "\uD83C\uDF43 Noviembre"
        12 -> "\uD83C\uDF84 Diciembre"
        else -> "Error, Mes no válido"
    }
    return "$mesString de $ano"
}

//Preview de muestra para gestionar diseno
@Preview(showBackground = true, widthDp = 400, heightDp = 600)
@Composable
fun PreviewCalendarioVista() {
    CalendarioVista(
        onDaySelected = { selectedDate ->
            println("Día seleccionado: $selectedDate") // Simulación de manejo de la selección
        },
        onRegistroClick = {
            println("Ir a la vista de registros (simulado)") // Simulación de cambio a la vista de registros
        }
    )
}
