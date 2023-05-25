package com.example.notesdemo.activity

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.Room
import com.example.notesdemo.R
import com.example.notesdemo.adapter.NotesAdapter
import com.example.notesdemo.animation.BtnAnimation
import com.example.notesdemo.databinding.ActivityMainBinding
import com.example.notesdemo.`interface`.MyListener
import com.example.notesdemo.model.NotesModel
import com.example.notesdemo.roomdatabaseclass.NoteDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var noteDatabase: NoteDatabase
    private lateinit var noteAdapter: NotesAdapter
    private lateinit var btnAnimation: BtnAnimation
    private val REQUEST_PHONE_CALL = 1
    val listNew: ArrayList<NotesModel> = ArrayList()
    var title = ""
    var content = ""
    var positionItemRv = ""
    var isClick = false
    var isLayoutGrid = false
    var ti = ""
    var des = ""
    var isZoomInOut = false
    var isThemeChange = false
    var isBottomSheet = false
    var serviceName: String = ""
    var phoneNumber = R.string.number
    lateinit var list: ArrayList<NotesModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        list = ArrayList()
        noteDatabase =
            Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "noteDB").build()

        binding.addNotes.setOnClickListener {
            binding.apply {
                notesContainer.visibility = View.VISIBLE
                openCloseBottomSheet(true)
                notesUpdateContainer.visibility = View.GONE
                rvItem.visibility = View.GONE
            }
        }

        binding.topArrow.setOnClickListener {
            openBottomSheets()
        }


        binding.iconLayoutChanger.setOnClickListener {
            if (!isLayoutGrid) {
                isLayoutGrid = true
                binding.iconLayoutChanger.setImageResource(R.drawable.grid)
                setLayout(2)
            } else {
                isLayoutGrid = false
                binding.iconLayoutChanger.setImageResource(R.drawable.linear)
                setLayout(1)
            }
        }

        binding.btnClear.setOnClickListener {
            clearSearch()
        }

        binding.rvItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // Perform actions when the scroll state changes
                // For example, you can check if the RecyclerView is currently scrolling or idle
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // RecyclerView is not scrolling
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // RecyclerView is currently being dragged by the user
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if (isBottomSheet) {
                        openBottomSheets()
                    }
                }
            }
        })

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                listNew.clear()
                val search = binding.edtSearch.text.toString()

                if (search.isNotEmpty()) {
                    binding.btnClear.visibility = View.VISIBLE
                    val strCHR: String = binding.edtSearch.text.toString()
                    for (l in 0 until list.size) {
                        serviceName = list[l].title.lowercase(Locale.getDefault())
                        if (serviceName.contains(strCHR.lowercase(Locale.getDefault()))) {
                            listNew.add(list[l])
                        }
                    }
                    if (isLayoutGrid) {
                        isLayoutGrid = false
                        setLayout(1)
                    } else {
                        isLayoutGrid = true
                        setLayout(2)
                    }

                    noteAdapter = NotesAdapter(applicationContext, listNew) { pos, image ->
                        clickRvItem(pos, listNew, image)
                    }

                    /*   noteAdapter.setOnItemClickListener(this@MainActivity)*/
                    binding.rvItem.adapter = noteAdapter
                } else {
                    binding.btnClear.visibility = View.GONE
                    clearSearch()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int,
            ) {
            }
        })

        binding.switchTheme.setOnClickListener {
            if (!isThemeChange) {
                isThemeChange = true
                binding.progress.visibility = View.VISIBLE
                binding.switchTheme.setImageResource(R.drawable.blackcircle)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.progress.visibility = View.INVISIBLE

            } else {
                binding.progress.visibility = View.VISIBLE
                isThemeChange = false
                binding.progress.visibility = View.INVISIBLE
                binding.switchTheme.setImageResource(R.drawable.whitecircle)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.contactUs.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    REQUEST_PHONE_CALL
                )
            } else {
                val phoneIntent = Intent(Intent.ACTION_CALL)
                phoneIntent.data = Uri.parse("tel:$phoneNumber")
                startActivity(phoneIntent)
            }
        }


        binding.aboutUs.setOnClickListener {
            openCloseBottomSheet(true)
            startActivity(Intent(this, AboutUs::class.java))

        }
        binding.zoomInOut.setOnClickListener {

            if (!isZoomInOut) {
                isZoomInOut = true
                binding.zoomInOut.setImageResource(R.drawable.zoominn)
                val layoutParams = binding.mainContainer.layoutParams
                layoutParams.width = 1080 // Set the desired width in pixels
                layoutParams.height = 1000 // Set the desired height in pixels
                binding.mainContainer.layoutParams = layoutParams
            }else{
                isZoomInOut = false
                binding.zoomInOut.setImageResource(R.drawable.zoomout)
                val layoutParams = binding.mainContainer.layoutParams
                layoutParams.width = 1080 // Set the desired width in pixels
                layoutParams.height = 2150 // Set the desired height in pixels
                binding.mainContainer.layoutParams = layoutParams
            }
        }
        binding.btnAdd.setOnClickListener {
            title = binding.edtNotesTitle.text.toString()
            content = binding.edtNotesContent.text.toString()


            if (title.isEmpty()) {
                val snackbar = Snackbar.make(
                    it,
                    getString(R.string.title_empty),
                    Snackbar.LENGTH_SHORT
                )
                val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)

                layoutParams.gravity = Gravity.TOP
                snackbar.setBackgroundTint(Color.RED).show()


            } else if (content.isEmpty()) {
                val snackbar = Snackbar.make(
                    it,
                    getString(R.string.contant_empty),
                    Snackbar.LENGTH_SHORT
                )
                val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)

                layoutParams.gravity = Gravity.TOP
                snackbar.setBackgroundTint(Color.RED).show()
            } else {

                GlobalScope.launch {
                    noteDatabase.noteDao().insert(
                        NotesModel(
                            0,
                            title,
                            content
                        )
                    )
                }
                binding.rvItem.visibility = View.VISIBLE
                binding.notesContainer.visibility = View.GONE
                getAllNotes()
            }
        }

    }

    private fun openCloseBottomSheet(result: Boolean) {
        binding.topArrow.setImageResource(R.drawable.bottomtotop)
        isBottomSheet = result
        openBottomSheets()
    }

    private fun openBottomSheets() {
        if (!isBottomSheet) {
            isBottomSheet = true

            binding.bottomSheet.visibility = View.VISIBLE
            val animate = TranslateAnimation(
                0f, 0f,
                binding.bottomSheet.height.toFloat(), 0f
            )

            // duration of animation
            animate.duration = 500
            animate.fillAfter = true

            binding.bottomSheet.startAnimation(animate)
            binding.topArrow.setImageResource(R.drawable.toptobottom)

        } else {
            isBottomSheet = false
            binding.topArrow.setImageResource(R.drawable.bottomtotop)
            val animate = TranslateAnimation(
                0f, 0f, 0f,
                binding.bottomSheet.height.toFloat()
            )
            animate.duration = 500
            binding.bottomSheet.startAnimation(animate)
            binding.bottomSheet.visibility = View.GONE
        }
    }

    private fun setLayout(i: Int) {
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(i, LinearLayoutManager.VERTICAL)
        binding.rvItem.layoutManager = staggeredGridLayoutManager
    }

    private fun clearSearch() {
        binding.edtSearch.text!!.clear()
        getAllNotes()

    }

    private fun getAllNotes() {
        noteDatabase.noteDao().getAll().observe(this) {
            list = ArrayList()
            Log.d("Data===================", it.toString())

            setLayout(2)
            list.addAll(it)

            noteAdapter = NotesAdapter(applicationContext, list) { pos, image ->
                clickRvItem(pos, list, image)
            }
            Log.d("Data===================", list.size.toString())
            binding.rvItem.adapter = noteAdapter
            noteAdapter.notifyDataSetChanged()
        }
    }


    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    @OptIn(DelicateCoroutinesApi::class)
    private fun clickRvItem(pos: Int, list: ArrayList<NotesModel>, image: View) {

        if (isBottomSheet) {
            isBottomSheet = true
            openCloseBottomSheet(true)
        }
        Log.d("List Array--------------- ", list.size.toString())
        Log.d("ListNew  Array------------- ", listNew.size.toString())
        isClick = true
        positionItemRv = pos.toString()

        binding.searchContainer.visibility = View.GONE
        binding.searchContainer.visibility = View.GONE
        binding.notesUpdateContainer.visibility = View.VISIBLE
        binding.rvItem.visibility = View.GONE

        binding.edtUpdateNotesTitle.setText(list[pos].title)
        binding.edtUpdateNotesContent.setText(list[pos].description)

        binding.btnUpdate.setOnClickListener {
            binding.searchContainer.visibility = View.VISIBLE
            ti = binding.edtUpdateNotesTitle.text.toString()
            des = binding.edtUpdateNotesContent.text.toString()

            GlobalScope.launch {
                list[pos].title = ti
                list[pos].description = des

                noteDatabase.noteDao().update(
                    NotesModel(
                        list[pos].id,
                        ti,
                        des
                    )
                )
            }
            val snackbar = Snackbar.make(
                findViewById(R.id.addNotes),
                getString(R.string.notes_update_successfully),
                Snackbar.LENGTH_SHORT
            )
            snackbar.setAction(
                R.string.undo
            ) {
                Toast.makeText(applicationContext, R.string.undo_action, Toast.LENGTH_SHORT).show()
            }
            snackbar.setBackgroundTint(Color.GREEN)
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.show()
            binding.rvItem.visibility = View.VISIBLE
            binding.notesUpdateContainer.visibility = View.GONE
            noteAdapter.notifyDataSetChanged()
        }

        binding.btnUpdateDelete.setOnClickListener {

            binding.searchContainer.visibility = View.VISIBLE

            GlobalScope.launch {
                noteDatabase.noteDao().delete(
                    NotesModel(
                        list[positionItemRv.toInt()].id,
                        ti,
                        des
                    )
                )
            }

            binding.rvItem.visibility = View.VISIBLE
            binding.notesUpdateContainer.visibility = View.GONE

            list.removeAt(positionItemRv.toInt())
            noteAdapter.notifyItemRemoved(positionItemRv.toInt())

            if (list.isEmpty()) {
                val snackbar = Snackbar.make(
                    findViewById(R.id.addNotes),
                    getString(R.string.no_data_found),
                    Snackbar.LENGTH_SHORT
                )
                snackbar.setAction(
                    getString(R.string.undo)
                ) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.undo_action),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                snackbar.setBackgroundTint(Color.RED)
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.show()
            }
        }
        binding.rvItem.visibility = View.GONE
        binding.notesUpdateContainer.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        var view: View? = null
        getAllNotes()
        binding.rvItem.visibility = View.VISIBLE

        binding.bottomSheet.setOnClickListener {
            Log.d(TAG, "clickRvItem: click")
        }
    }

    fun addContainerClose(view: View) {
        binding.notesContainer.visibility = View.GONE
        binding.rvItem.visibility = View.VISIBLE
        binding.searchContainer.visibility = View.VISIBLE
    }

    fun updateContainerClose(view: View) {
        binding.notesUpdateContainer.visibility = View.GONE
        binding.rvItem.visibility = View.VISIBLE
        binding.searchContainer.visibility = View.VISIBLE
    }

    fun closeApp(view: View) {
        onBackPressed()
    }
}
