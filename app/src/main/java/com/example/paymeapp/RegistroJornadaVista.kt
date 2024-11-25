package com.example.paymeapp

import android.annotation.SuppressLint
import android.app.BackgroundServiceStartNotAllowedException
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RegistroJornadaVista(
    fecha: String,
    onSave: (RegistroJornada)->Unit
) {
    //val scrollState = rememberScrollState()

    // Usamos `rememberSaveable` para las variables persistentes
    var horaEntrada by rememberSaveable { mutableStateOf("") }
    var horaSalida by rememberSaveable { mutableStateOf("") }
    var precioHora by rememberSaveable { mutableStateOf("") }
        //var resultado by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de Jornada",
            style = TextStyle(
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Picker de Hora de Entrada
        TimePickerField(
            label = "Hora de Entrada",
            selectedTime = horaEntrada,
            onTimeSelected = { horaEntrada = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Picker de Hora de Salida
        TimePickerField(
            label = "Hora de Salida",
            selectedTime = horaSalida,
            onTimeSelected = { horaSalida = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Menú de precios por hora
        DropdownMenuField(
            selectedOption = precioHora,
            onOptionSelected = { precioHora = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para calcular
        Button(
            onClick = {
                //resultado = RegistroJornadaControlador.calcularHorasTrabajadas(
                    //horaEntrada,
                    //horaSalida,
                    //precioHora
                val (horasTrabajadas, sueldoGanado) = RegistroJornadaControlador.calcularHorasTrabajadas(
                    horaEntrada,
                    horaSalida,
                    precioHora
                )
            //Crear registro de jornada y pasar al DAO
            val registro = RegistroJornada(fecha, horasTrabajadas, sueldoGanado)
            onSave(registro)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Guardar", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resultado
        /*Text(
            text = resultado,
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )*/
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimePickerField(
    label: String,
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            onTimeSelected(String.format("%02d:%02d", hour, minute))
        },
        0,
        0,
        true
    )

    OutlinedTextField(
        value = selectedTime,
        onValueChange = {},
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { timePickerDialog.show() }) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
                    contentDescription = null,
                    tint = if (selectedTime.isNotEmpty()) Color.Blue else Color.Gray
                )
            }
        },
        readOnly = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun DropdownMenuField(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text("Precio por hora") },
            trailingIcon = {
                IconButton(onClick = { expandido = !expandido }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.arrow_down_float),
                        contentDescription = null,
                        tint = if (expandido) Color.Blue else Color.Gray
                    )
                }
            },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            listOf(
                "Día natural 6.20€/h",
                "Noche natural 8€/h",
                "Día festivo/Finde 8€/h",
                "Noche festivo/Finde 10€/h"
            ).forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expandido = false
                    }
                )
            }
        }
    }
}


