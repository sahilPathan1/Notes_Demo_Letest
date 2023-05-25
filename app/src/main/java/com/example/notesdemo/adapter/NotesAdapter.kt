package com.example.notesdemo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesdemo.activity.DownloadData
import com.example.notesdemo.databinding.ItemNotesListBinding
import com.example.notesdemo.`interface`.MyListener
import com.example.notesdemo.model.NotesModel


class NotesAdapter(
    var context: Context,
    var notesList: ArrayList<NotesModel>,
    var callBack: (Int, View) -> Unit,
):
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

        var isDownload = false
 /*   private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }*/


   /* init {
        notesList.reverse()


    }*/

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

        (notesList[position])

        holder.binding.downloadDoc.setOnClickListener {

            val intent = Intent(context, DownloadData::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("title", notesList[position].title)
            intent.putExtra("description", notesList[position].description)
            context.startActivity(intent)

        }
        holder.itemView.setOnClickListener {
                callBack.invoke(position, holder.binding.downloadDoc)
        }
    }

    override fun getItemCount() = notesList.size
}


