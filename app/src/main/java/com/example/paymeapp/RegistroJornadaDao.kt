package com.example.paymeapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RegistroJornadaDao {

    @Update //actuliza automaticamente por ID
    suspend fun actualizar(registroJornada : RegistroJornada)

    @Insert
    suspend fun insertar(registroJornada: RegistroJornada)

    @Delete
    suspend fun borrar(registroJornada: RegistroJornada)

    @Query("SELECT * FROM registro_jornada WHERE dia = :dia")
    suspend fun obtenerRegistro(dia: String): RegistroJornada?// evitar el null

    // Suspen fun hace que las fucniones sean funciones de suspension, se puede usar dentro de cualquier bloque y libera el main
    @Query("SELECT * FROM registro_jornada")
    suspend fun obtenerTodo(): List<RegistroJornada>
}
