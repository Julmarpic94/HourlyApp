package com.example.paymeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getInstance(applicationContext) // Inicialización de la base de datos

        setContent {
            // Usa rememberSaveable para persistir el estado
            var selectedDate by remember { mutableStateOf<String?>(null) }

            MaterialTheme {
                if (selectedDate == null) {
                    // Mostrar el calendario
                    CalendarioVista { date ->
                        selectedDate = date
                    }
                } else {
                    // Mostrar el formulario de registro
                    RegistroJornadaVista(
                        fecha = selectedDate!!,
                        onSave = { registro ->
                            lifecycleScope.launch {
                                try {
                                    database.registroJornadaDao().insertar(registro)
                                    selectedDate = null // Volver al calendario
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
