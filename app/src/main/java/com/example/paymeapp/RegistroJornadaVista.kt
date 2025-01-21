package com.example.paymeapp

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.paymeapp.ui.theme.AzulClaro
import com.example.paymeapp.ui.theme.AzulOscuro

//PANTALLA VISTA PARA REGISTRAR LA JORNADA
@Composable
fun RegistroJornadaVista(
    fecha: String,
    guardar: () -> Unit // funcion para volver al guardar
) {
    // Usamos `rememberSaveable` para las variables persistentes
    var horaEntrada by rememberSaveable { mutableStateOf("") }
    var horaSalida by rememberSaveable { mutableStateOf("") }
    var precioHora by rememberSaveable { mutableStateOf("") }
    var resultadoTexto by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Día: $fecha",
            style = TextStyle(
                color = AzulOscuro,
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
                resultadoTexto = calcularRegistroCompleto(
                    fecha = fecha,
                    horaEntrada = horaEntrada,
                    horaSalida = horaSalida,
                    precioHora = precioHora
                )
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
            Text(text = "Calcular", style = TextStyle(fontSize = 16.sp))
        }
        Spacer(modifier = Modifier.height(24.dp))

        //Boton para guardar Registro, en lista y en DB
        Button(
            onClick = {
                //validamos los campos
                if (horaEntrada.isEmpty() || horaSalida.isEmpty() || precioHora.isEmpty()) {
                    resultadoTexto = "Por favor, completa todos los campos"
                    return@Button

                }

                //Crear el registro
                val registroFB = RegistroFirestore(
                    fecha = fecha,
                    horasTrabajadas = calcularHorasTrabajadas(horaEntrada, horaSalida),
                    dineroGanado = calcularDineroGanado(
                        calcularHorasTrabajadas(horaEntrada, horaSalida), precioHora
                    )
                )
                //GUARDAR EN FIRESTORE
                guardarFirestore(registroFB)

                //Mostrar




                guardar()
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
            Text(text = "Guardar", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resultado
        OutlinedTextField(
            value = resultadoTexto,
            onValueChange = {},
            label = { Text("Resultado") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


//Pikcers para capturar las horas
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

//Menu desplegable para gestionar el precio de las horas
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


//Vista Previa
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PreviewRegistroJornadaVista() {
    RegistroJornadaVista(
        fecha = "2024-11-25",
        guardar = {} // No hace nada; solo es necesario para cumplir con la firma
    )
}





