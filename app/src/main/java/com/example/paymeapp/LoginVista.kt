package com.example.paymeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paymeapp.ui.theme.AzulClaro
import com.example.paymeapp.ui.theme.AzulOscuro
import com.example.paymeapp.ui.theme.Blanco
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginVista(
    onLoginSuccess: () -> Unit,
) {
    // Variables de estado para los campos de texto
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blanco)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Image(
            painter = painterResource(id = R.drawable.logosombra), // Reemplaza con tu recurso de imagen
            contentDescription = "Login Image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        // Título de la pantalla
        Text(
            text = "\"Porque tu tiempo importa\"",
            style = TextStyle(
                color = AzulOscuro,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Campo de texto: Usuario
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") }, // Etiqueta en gris para contraste
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(8.dp)) // Bordes redondeados para mejor diseño
        )


        // Campo de texto: Contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        // Botones: Entrar
        Button(
            onClick = {
                autentificar(
                    usuario,
                    contrasena,
                    onSuccess = {
                        onLoginSuccess()//Accedemos a la APP
                    },
                    onError = { error -> mensajeError = error }

                )
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
            Text(text = "Entrar", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón: Login
        Button(
            onClick = {

                registrar(usuario,contrasena,
                    onSuccess = {mensajeError = "Usuario registrado Correctamente"},
                    onError = { error -> mensajeError = error}
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
            Text(text = "Registro", style = TextStyle(fontSize = 16.sp))
        }
        // Mensaje de Acceso no válido
        if (mensajeError.isNotEmpty()){
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensajeError,
                color = if (mensajeError == "Bienvenido") Color.Green else Color.Red,
                style = TextStyle( fontSize = 14.sp, fontWeight = FontWeight.Bold)
            )
        }

    }
}


//Autentificar con Firebase
fun autentificar(usuario: String,
                 contrasena: String,
                 onSuccess:() -> Unit,
                 onError: (String)->Unit
                 ) {
    if (usuario.isNotEmpty() && contrasena.isNotEmpty()) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error al autentificar.")
                }
            }
    } else {
        onError("Campos vácios")
    }
}

//REGISTRAR
fun registrar(
    usuario: String,
                 contrasena: String,
                 onSuccess:() -> Unit,
                 onError: (String)->Unit
                 ) {
    if (usuario.isNotEmpty() && contrasena.isNotEmpty()) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Error, usuario ya registrado.")
                }
            }
    } else {
        onError("Campos vácios")
    }
}


//Vista Previa
@Preview(showBackground = true, name = "Login Vista")
@Composable
fun PreviewLogin() {
}
