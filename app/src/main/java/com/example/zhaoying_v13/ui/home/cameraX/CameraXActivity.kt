package com.example.zhaoying_v13.ui.home.cameraX;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.zhaoying_v13.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class CameraXActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private PreviewView mPreviewView;
    private com.example.zhaoying_v13.ui.home.cameraX.LongClickView mIvCamera;
    private ImageView mIvReverse;
    private ListenableFuture<ProcessCameraProvider> mProcessCameraProviderListenableFuture;
    private ImageCapture mImageCapture;
    private boolean isFront;//是否开启前置摄像头，默认false
    private Camera mCamera;
    private VideoCapture mVideoCapture;
    private ProcessCameraProvider mProcessCameraProvider;
    private CameraSelector mCameraSelector;
    private Preview mPreview;
    private ImageAnalysis mImageAnalysis;
    private int mPermissionGranted;
    private boolean mIsLessOneMin;//这次拍摄的视频是否不足一分钟


    @SuppressLint({"ClickableViewAccessibility", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_x);
        Toast.makeText(this, "轻触拍照，长按录制视频", Toast.LENGTH_SHORT).show();
        mPreviewView = (PreviewView) findViewById(R.id.preview);
        mIvCamera = (com.example.zhaoying_v13.ui.home.cameraX.LongClickView) findViewById(R.id.iv_camera1);
        mIvReverse = (ImageView) findViewById(R.id.iv_reverse1);

        requestPermission();
        mPermissionGranted = PackageManager.PERMISSION_GRANTED;
        mIvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        mIvCamera.setMyClickListener(new com.example.zhaoying_v13.ui.home.cameraX.LongClickView.MyClickListener() {
            @Override
            public void longClickFinish() {
                //手指抬起，关闭录像,可能是因为还没开启的原因，所以就算调用停止也不会走回调去保存
                mVideoCapture.stopRecording();
            }

            @Override
            public void singleClickFinish() {
                takePhoto();
                mIsLessOneMin = true;
                mVideoCapture.stopRecording();
            }

            @Override
            public void longClickStart() {
                mIsLessOneMin = false;
                startVideo();
            }
        });
        mPreviewView.setOnTouchListener((view, motionEvent) -> {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            FocusMeteringAction focusMeteringAction = new FocusMeteringAction.Builder(mPreviewView.getMeteringPointFactory()
                    .createPoint(x, y)).build();
            mCamera.getCameraControl().startFocusAndMetering(focusMeteringAction);
            return true;
        });
        mIvReverse.setOnClickListener(v -> {
            isFront = !isFront;
            initCamera();
        });
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != mPermissionGranted
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != mPermissionGranted
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != mPermissionGranted) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            initCamera();
        }
    }


    private void initCamera() {
        mProcessCameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        mProcessCameraProviderListenableFuture.addListener(() -> {
            try {
                mProcessCameraProvider = mProcessCameraProviderListenableFuture.get();
                bindPreview(mProcessCameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @SuppressLint("RestrictedApi")
    private void takePhoto() {
        String path = com.example.zhaoying_v13.ui.home.cameraX.Constants.getFilePath() + File.separator + System.currentTimeMillis() + ".jpg";
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions
                .Builder(new File(path)).build();
        mImageCapture.takePicture(outputFileOptions, CameraXExecutors.mainThreadExecutor(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(CameraXActivity.this, "图片以保存" + path, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(CameraXActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void startVideo() {
        Log.e(TAG, "startVideo: ");
        String path = com.example.zhaoying_v13.ui.home.cameraX.Constants.getFilePath() + File.separator + System.currentTimeMillis() + ".mp4";
        Log.e(TAG, "path:"+path);
        VideoCapture.OutputFileOptions build = new VideoCapture.OutputFileOptions.Builder(new File(path)).build();
        mVideoCapture.startRecording(build, CameraXExecutors.mainThreadExecutor(), new VideoCapture.OnVideoSavedCallback() {
            @Override
            public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                if (mIsLessOneMin) {
                    new File(path).delete();
                } else {
                    Toast.makeText(CameraXActivity.this, "视频已保存" + outputFileResults.getSavedUri().getPath(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                Log.e(TAG, "onError: " + message);
                new File(path).delete();//视频不足一秒会走到这里来，但是视频依然生成了，所以得删掉
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void bindPreview(ProcessCameraProvider processCameraProvider) {
        //创建preview
        mPreview = new Preview.Builder().build();
        //指定所需的相机选项,设置摄像头镜头切换
        mCameraSelector = new CameraSelector.Builder().requireLensFacing(isFront ? CameraSelector.LENS_FACING_FRONT :
                CameraSelector.LENS_FACING_BACK).build();
        //将 Preview 连接到 PreviewView。
        mPreview.setSurfaceProvider(mPreviewView.getSurfaceProvider());
        //将所选相机和任意用例绑定到生命周期。

        mImageCapture = new ImageCapture.Builder()
                .setTargetRotation(mPreviewView.getDisplay().getRotation())
                .build();

        mVideoCapture = new VideoCapture.Builder()
                .setTargetRotation(mPreviewView.getDisplay().getRotation())
                .setVideoFrameRate(25)//每秒的帧数
                .setBitRate(3 * 1024 * 1024)//设置每秒的比特率
                .build();

        processCameraProvider.unbindAll();
        mCamera = processCameraProvider.bindToLifecycle(this, mCameraSelector,
                mImageCapture, mVideoCapture, mPreview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        if (requestCode == 1) {
            initCamera();
        }
    }

}
