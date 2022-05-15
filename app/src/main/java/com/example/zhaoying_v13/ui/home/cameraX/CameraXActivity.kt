package com.example.zhaoying_v13.ui.home.cameraX

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.zhaoying_v13.databinding.ActivityCameraxBinding
import com.google.android.material.tabs.TabLayout
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.io.File

class CameraXActivity : AppCompatActivity() {
    private lateinit var poseDetector: PoseDetector
    private lateinit var videoCapture: VideoCapture
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var binding: ActivityCameraxBinding
    private lateinit var filepath: String

    @SuppressLint("MissingPermission", "RestrictedApi", "UnsafeExperimentalUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnStart = binding.btnStart
        btnStop = binding.btnStop

        btnStart.setOnClickListener {
            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + "DCIM/Camera",
                "${System.currentTimeMillis()}.mp4"
            )
            filepath = file.path

            val outputFileOptions = VideoCapture.OutputFileOptions.Builder(file).build()


            videoCapture.startRecording(
                outputFileOptions,
                ContextCompat.getMainExecutor(this),
                object : VideoCapture.OnVideoSavedCallback {
                    override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                        Log.d("Check:", "On Video Saved")
                    }

                    override fun onError(
                        videoCaptureError: Int,
                        message: String,
                        cause: Throwable?
                    ) {
                        Log.d("Check:", "On Video Error" + message)
                    }
                }
            )
        }

        btnStop.setOnClickListener {
            Toast.makeText(this, String.format("video is save at %s", filepath), Toast.LENGTH_LONG)
                .show()
            videoCapture.stopRecording()
        }

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)

        }, ContextCompat.getMainExecutor(this))

        val options = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()


        poseDetector = PoseDetection.getClient(options)


    }

    @SuppressLint("RestrictedApi", "UnsafeExperimentalUsageError", "NewApi")
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {

        Log.d("Check:", "inside bind preview")


        val preview = Preview.Builder().build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val point = Point()

        videoCapture = VideoCapture.Builder()
            .setTargetResolution(Size(point.x,point.y))
            .build()


        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(Size(point.x,point.y))
            .build()


        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            ImageAnalysis.Analyzer { imageProxy ->

                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                val image = imageProxy.image

                if (image != null) {

                    val processImage = InputImage.fromMediaImage(image, rotationDegrees)

                    poseDetector.process(processImage)
                        .addOnSuccessListener {

                            if (binding.parentLayout.childCount > 3) {
                                binding.parentLayout.removeViewAt(3)
                            }
                            if (it.allPoseLandmarks.isNotEmpty()) {

                                if (binding.parentLayout.childCount > 3) {
                                    binding.parentLayout.removeViewAt(3)
                                }
                                Log.e(
                                    "DEBUG",
                                    it.getPoseLandmark(PoseLandmark.LEFT_ANKLE).toString()
                                )
                                val element = Draw(applicationContext, it)
                                binding.parentLayout.addView(element)
                            }
                        }
                        .addOnFailureListener {
                            imageProxy.close()
                        }.addOnCompleteListener {
                            imageProxy.close()
                        }
                }


            })

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview, videoCapture)

    }
}