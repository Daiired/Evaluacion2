package com.example.evaluacion2.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Elementos(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    var elemento:String,
    var comprado:Boolean
)