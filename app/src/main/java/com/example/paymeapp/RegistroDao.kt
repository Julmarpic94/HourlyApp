package com.example.paymeapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


// DAO PARA GESTIONAR LOS METODOS QUE LLAMAN A LA BASE DE DATOS Y HACER OPS CRUD
@Dao
interface RegistroDao {

    @Insert
    suspend fun insertar(registro: Registro)

    @Query("SELECT * FROM registros WHERE id = :id")
    suspend fun obtenerRegistroId(id: Int): Registro?

    @Query("SELECT * FROM registros")
    suspend fun obtenerTodos(): List<Registro>

    @Query("DELETE FROM registros WHERE id = :id")
    suspend fun borrarPorId(id: Int)

    @Query("DELETE FROM registros")
    suspend fun borrarTodo()


}
