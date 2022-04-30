package com.example.zhaoying_v13.ui.home.cameraDec

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.zhaoying_v13.databinding.ActivityCameraDecBinding


class CameraDecActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_VIDEO_CAPTURE = 1
    }
    private lateinit var binding: ActivityCameraDecBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraDecBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dispatchTakeVideoIntent()

    }

    private fun dispatchTakeVideoIntent() {




        val values = ContentValues()
        values.put(MediaStore.Video.Media.TITLE, "MyVideo")
        values.put(
            MediaStore.Video.Media.DESCRIPTION,
            "Video taken on " + System.currentTimeMillis()
        )
        imageUri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)

//        Intent(MediaStore.ACTION_VIDEO_CAPTURE).
//        putExtra(MediaStore.EXTRA_OUTPUT, imageUri).also { takeVideoIntent ->
//            takeVideoIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(takeVideoIntent, Companion.REQUEST_VIDEO_CAPTURE)
//
//            }
//        }
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takeVideoIntent, Companion.REQUEST_VIDEO_CAPTURE)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri? = data?.data
            Log.i("TAGCamera", videoUri.toString())
            binding.videoView.setVideoURI(videoUri)
            Log.i("TAGCamera", getFilePathByUri(videoUri)!!)

            binding.videoView.start()
        }
    }

    fun getFilePathByUri(uri: Uri?): String? {
        val selectedImage = uri
        val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor =
            selectedImage?.let {
                this.contentResolver.query(
                    it,
                    filePathColumns,
                    null,
                    null,
                    null
                )
            }!!
        c.moveToFirst()
        val columnIndex = c.getColumnIndex(filePathColumns[0])
        val imagePath = c.getString(columnIndex)
        return imagePath
    }


}