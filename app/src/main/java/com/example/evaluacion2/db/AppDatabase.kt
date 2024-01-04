package com.example.evaluacion2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Elementos::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract  fun elementosDao(): ElementosDao

    companion object{
        @Volatile
        private var BASE_DATOS: AppDatabase? = null

        fun getInstance(contexto:Context): AppDatabase{
            return BASE_DATOS ?: synchronized(this){
                Room.databaseBuilder(
                    contexto.applicationContext,
                    AppDatabase::class.java,
                    "tareas.bd"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also{ BASE_DATOS = it}
            }
        }
    }
}