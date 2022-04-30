package com.example.zhaoying_v13.ui.home.detection

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.databinding.ActivityCameraDecBinding
import com.example.zhaoying_v13.ui.home.cameraDec.CameraDecActivity


class DetectionActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCameraDecBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detection)

    }


}

