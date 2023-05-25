package com.example.notesdemo.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.notesdemo.R
import com.example.notesdemo.databinding.ActivityDownloadDataBinding
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream

class DownloadData : AppCompatActivity() {
    var pageHeight = 1120
    var pageWidth = 792
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap
    var PERMISSION_CODE = 101
    private lateinit var binding: ActivityDownloadDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_download_data)
        binding.apply {
            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("description")

            tvTitle.text = title
            tvDescription.text = description

            bmp = BitmapFactory.decodeResource(resources, R.drawable.add)
            scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

            if (checkPermissions()) {
                Toast.makeText(this@DownloadData, "Permissions Granted..", Toast.LENGTH_SHORT)
                    .show()
            } else {
                requestPermission()
            }

            btnDownload.setOnClickListener {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    // on below line we are calling generate
                    // PDF method to generate our PDF file.
                  /*  generatePDF(title, description)*/

                    generatePdfDocument(title,description)
                }
            }
        }
    }

    private fun generatePdfDocument(title: String?, description: String?) {
        val document = Document()

        val fileName = "sample.pdf"
        val filePath = getExternalFilesDir(null)?.absolutePath + "/" + fileName

        try{
            PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            val snackbar = Snackbar.make(
                findViewById(R.id.btnDownload),
                getString(R.string.genrate),
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

        }catch (e:java.lang.Exception){
            Toast.makeText(this, "error$e", Toast.LENGTH_SHORT).show()
        }

        val text = "This is a sample PDF document!"
        val paragraph = Paragraph(title.toString() +"\\\n"+ description.toString())
        document.add(paragraph)
        document.close()
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
        )
    }


    private fun checkPermissions(): Boolean {
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )
        var readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}