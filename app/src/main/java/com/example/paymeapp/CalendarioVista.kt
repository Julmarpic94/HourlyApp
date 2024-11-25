package com.example.paymeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun CalendarioVista(onDaySelected: (String) -> Unit) {
    val scrollState = rememberScrollState()

    // Información del calendario
    val calendario = Calendar.getInstance()
    val diasMes = remember { mutableStateListOf<Int>() }
    calendario.set(Calendar.DAY_OF_MONTH, 1)
    val totalDias = calendario.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Genera los días del mes actual
    LaunchedEffect(Unit) {
        diasMes.clear()
        diasMes.addAll(1..totalDias)
    }

    // Contenedor principal con desplazamiento
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Texto "Calendario"
        Text(
            text = "Calendario",
            style = TextStyle(
                color = Color.Black,
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

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Altura fija para el calendario
            ) {
                items(diasMes) { day ->
                    val selectedDate = "${calendario.get(Calendar.YEAR)}-${calendario.get(Calendar.MONTH) + 1}-$day"
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .aspectRatio(1f)
                            .clickable { onDaySelected(selectedDate) }, // Detecta el click en el día
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
        }
    }
}

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
