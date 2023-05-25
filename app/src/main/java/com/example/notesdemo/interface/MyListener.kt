package com.example.notesdemo.`interface`

import com.example.notesdemo.model.NotesModel

interface MyListener {
    fun onItemClick(data: NotesModel,pos:Int)
}