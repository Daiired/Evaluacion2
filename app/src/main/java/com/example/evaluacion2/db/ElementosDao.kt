package com.example.evaluacion2.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ElementosDao {

    @Query("SELECT * FROM elementos ORDER BY comprado")
    fun findAll():List<Elementos>

    @Insert
    fun insertar(elementos: Elementos): Long

    @Update
    fun actualizar(elementos: Elementos)

    @Delete
    fun eliminar(elementos: Elementos)
}