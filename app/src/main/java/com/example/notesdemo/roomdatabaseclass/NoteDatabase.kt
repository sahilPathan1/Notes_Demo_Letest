package com.example.notesdemo.roomdatabaseclass

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesdemo.dao.NoteDao
import com.example.notesdemo.model.NotesModel


@Database(entities = [NotesModel::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
