package com.example.zhaoying_v13.ui.home.cameraX

import android.Manifest
import androidx.camera.view.PreviewView
import androidx.camera.lifecycle.ProcessCameraProvider
import android.annotation.SuppressLint
import android.os.Bundle
import com.example.zhaoying_v13.R
import android.widget.Toast
import android.content.pm.PackageManager
import android.graphics.Point
import android.util.Log
import android.util.Size
import com.example.zhaoying_v13.ui.home.cameraX.LongClickView.MyClickListener
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.zhaoying_v13.databinding.ActivityCameraXBinding
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.io.File
import java.util.concurrent.ExecutionException

class CameraXActivity : AppCompatActivity() {
    private var mPreviewView: PreviewView? = null
    private var mIvCamera: LongClickView? = null
    private var mIvReverse: ImageView? = null
    private var mProcessCameraProviderListenableFuture: ListenableFuture<ProcessCameraProvider>? =
        null
    private var mImageCapture: ImageCapture? = null
    private var isFront //是否开启前置摄像头，默认false
            = false
    private var mCamera: Camera? = null
    private var mVideoCapture: VideoCapture? = null
    private var mProcessCameraProvider: ProcessCameraProvider? = null
    private var mCameraSelector: CameraSelector? = null
    private var mPreview: Preview? = null
    private val mImageAnalysis: ImageAnalysis? = null
    private var mPermissionGranted = 0
    private var mIsLessOneMin //这次拍摄的视频是否不足一分钟
            = false

    private lateinit var poseDetector: PoseDetector
    private lateinit var binding: ActivityCameraXBinding

    @SuppressLint("ClickableViewAccessibility", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this, "轻触拍照，长按录制视频", Toast.LENGTH_SHORT).show()
        mPreviewView = findViewById<View>(R.id.preview) as PreviewView
        mIvCamera = findViewById<View>(R.id.iv_camera1) as LongClickView
        mIvReverse = findViewById<View>(R.id.iv_reverse1) as ImageView
        requestPermission()
        mPermissionGranted = PackageManager.PERMISSION_GRANTED
        mIvCamera!!.setOnClickListener { takePhoto() }
        mIvCamera!!.setMyClickListener(object : MyClickListener {
            override fun longClickFinish() {
                //手指抬起，关闭录像,可能是因为还没开启的原因，所以就算调用停止也不会走回调去保存
                mVideoCapture!!.stopRecording()
            }

            override fun singleClickFinish() {
                takePhoto()
                mIsLessOneMin = true
                mVideoCapture!!.stopRecording()
            }

            override fun longClickStart() {
                mIsLessOneMin = false
                startVideo()
            }
        })
        mPreviewView!!.setOnTouchListener { view: View?, motionEvent: MotionEvent ->
            val x = motionEvent.x
            val y = motionEvent.y
            val focusMeteringAction = FocusMeteringAction.Builder(
                mPreviewView!!.meteringPointFactory
                    .createPoint(x, y)
            ).build()
            mCamera!!.cameraControl.startFocusAndMetering(focusMeteringAction)
            true
        }
        mIvReverse!!.setOnClickListener { v: View? ->
            isFront = !isFront
            initCamera()
        }

        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()

        poseDetector = PoseDetection.getClient(options)
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != mPermissionGranted || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != mPermissionGranted || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != mPermissionGranted
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
                ), 1
            )
        } else {
            initCamera()
        }
    }

    private fun initCamera() {
        mProcessCameraProviderListenableFuture = ProcessCameraProvider.getInstance(this)
        mProcessCameraProviderListenableFuture!!.addListener({
            try {
                mProcessCameraProvider = mProcessCameraProviderListenableFuture!!.get()
                bindPreview(mProcessCameraProvider)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("RestrictedApi")
    private fun takePhoto() {
        val path = Constants.filePath + File.separator + System.currentTimeMillis() + ".jpg"
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(File(path)).build()
        mImageCapture!!.takePicture(
            outputFileOptions,
            CameraXExecutors.mainThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@CameraXActivity, "图片以保存$path", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraXActivity, exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    @SuppressLint("RestrictedApi")
    private fun startVideo() {
        Log.e(TAG, "startVideo: ")
        val path = Constants.filePath + File.separator + System.currentTimeMillis() + ".mp4"
        Log.e(TAG, "path:$path")
        val build = VideoCapture.OutputFileOptions.Builder(File(path)).build()
        mVideoCapture!!.startRecording(
            build,
            CameraXExecutors.mainThreadExecutor(),
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                    if (mIsLessOneMin) {
                        File(path).delete()
                    } else {
                        Toast.makeText(
                            this@CameraXActivity, "视频已保存" + outputFileResults.savedUri!!
                                .path, Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    Log.e(TAG, "onError: $message")
                    File(path).delete() //视频不足一秒会走到这里来，但是视频依然生成了，所以得删掉
                }
            })
    }

    @SuppressLint("RestrictedApi", "UnsafeOptInUsageError")
    private fun bindPreview(processCameraProvider: ProcessCameraProvider?) {
        //创建preview
        mPreview = Preview.Builder().build()
        //指定所需的相机选项,设置摄像头镜头切换
        mCameraSelector = CameraSelector.Builder()
            .requireLensFacing(if (isFront) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK)
            .build()

        //将 Preview 连接到 PreviewView。
        mPreview!!.setSurfaceProvider(mPreviewView!!.surfaceProvider)


        //将所选相机和任意用例绑定到生命周期。
        mImageCapture = ImageCapture.Builder()
            .setTargetRotation(mPreviewView!!.display.rotation)
            .build()
        mVideoCapture = VideoCapture.Builder()
            .setTargetRotation(mPreviewView!!.display.rotation)
            .setVideoFrameRate(25) //每秒的帧数
            .setBitRate(3 * 1024 * 1024) //设置每秒的比特率
            .build()

        val point = Point()

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(Size(point.x, point.y))
            .build()

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            ImageAnalysis.Analyzer { imageProxy ->

                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                val image = imageProxy.image

                if (image != null) {

                    val processImage = InputImage.fromMediaImage(image, rotationDegrees)

                    var parentLayout = binding.container

                    poseDetector.process(processImage)
                        .addOnSuccessListener {


                            if (parentLayout.childCount > 3) {
                                parentLayout.removeViewAt(3)
                            }
                            if (it.allPoseLandmarks.isNotEmpty()) {

                                Toast.makeText(
                                    this,
                                    it.allPoseLandmarks.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                                if (parentLayout.childCount > 3) {
                                    parentLayout.removeViewAt(3)
                                }

                                val element = Draw(applicationContext, it)
                                parentLayout.addView(element)
                            }
                            imageProxy.close()
                        }
                        .addOnFailureListener {

                            imageProxy.close()
                        }
                }
            })



        processCameraProvider!!.unbindAll()
        mCamera = processCameraProvider.bindToLifecycle(
            this, mCameraSelector!!,
            mImageCapture, mVideoCapture, mPreview
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }
        if (requestCode == 1) {
            initCamera()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}