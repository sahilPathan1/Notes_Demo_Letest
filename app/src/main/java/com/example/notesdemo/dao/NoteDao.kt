package com.example.notesdemo.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesdemo.model.NotesModel

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: NotesModel)

    @Update
    suspend fun update(note: NotesModel)

    @Delete
    suspend fun delete(note: NotesModel)

    @Query("SELECT  * FROM myData")
    fun getAll(): LiveData<List<NotesModel>>
}
