package com.example.notesdemo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myData")
data class NotesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var description: String
)
