package com.example.notesdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notesdemo.databinding.ItemNotesListBinding
import com.example.notesdemo.model.NotesModel


class NotesAdapter(
    var context: Context,
    var notesList: ArrayList<NotesModel>,
    var callBack: (Int) -> Unit,
) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(){

    init {
        notesList.reverse()
    }

    inner class NoteViewHolder(var binding: ItemNotesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNotesListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notesList[position]
        holder.binding.tvTitle.text = currentNote.title
        holder.binding.tvContent.text = currentNote.description

        holder.itemView.setOnClickListener {
            callBack.invoke(position)
        }
    }

    override fun getItemCount() = notesList.size
}
