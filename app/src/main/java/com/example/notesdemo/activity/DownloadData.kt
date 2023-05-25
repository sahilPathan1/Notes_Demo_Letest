package com.example.notesdemo.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.notesdemo.R
import com.example.notesdemo.databinding.ActivityDownloadDataBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
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
                    generatePDF(title, description)
                }
            }

        }
    }

    private fun generatePDF(title: String?, description: String?) {
        var pdfDocument: PdfDocument = PdfDocument()

        var paint: Paint = Paint()
        var title: Paint = Paint()

        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        var canvas: Canvas = myPage.canvas

        canvas.drawBitmap(scaledbmp, 56F, 40F, paint)

        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        title.textSize = 15F

        title.color = ContextCompat.getColor(this, R.color.purple_200)

        canvas.drawText(title.toString(), 209F, 100F, title)
        canvas.drawText(description.toString(), 209F, 80F,title)
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.purple_200)
        title.textSize = 15F

        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)
        pdfDocument.finishPage(myPage)

        val fileOp  = File(applicationContext.getExternalFilesDir(null), "Demo.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(fileOp))

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

        } catch (e: Exception) {

            val snackbar = Snackbar.make(
                findViewById(R.id.btnDownload),
                getString(R.string.error),
                Snackbar.LENGTH_SHORT
            )
            snackbar.setAction(
                R.string.undo
            ) {
                Toast.makeText(applicationContext, R.string.undo_action, Toast.LENGTH_SHORT).show()
            }
            snackbar.setBackgroundTint(Color.RED)
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.show()
            e.printStackTrace()
        }

        pdfDocument.close()
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