package com.example.zhaoying_v13.ui.home

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.databinding.ActivityMainBinding
import com.example.zhaoying_v13.databinding.ActivitySelectBinding
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission


class SelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val btnSelectFile = binding.btnSelectFile
        btnSelectFile.setOnClickListener {
            Log.i("Tag", "按钮有效")
            AndPermission.with(this@SelectActivity)
                .runtime()
                .permission(
                    Permission.WRITE_EXTERNAL_STORAGE,
                    Permission.READ_EXTERNAL_STORAGE
                )
                .onGranted(object : Action<List<String?>?> {
                    override fun onAction(data: List<String?>?) {
                        // 申请的权限全部允许
                        //调用相册
                        val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(intent, 1)
                    }
                })
                .onDenied(object : Action<List<String?>?> {
                    override fun onAction(data: List<String?>?) {
                        TODO("Not yet implemented")
                    }
                })
                .start()
        }
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
            val c: Cursor =
                selectedImage?.let { getContentResolver().query(it, filePathColumns, null, null, null) }!!
            c.moveToFirst()
            val columnIndex = c.getColumnIndex(filePathColumns[0])
            val imagePath = c.getString(columnIndex)


//            binding.textField.
            binding.inputTextFiled.setText(imagePath)
            Log.i("TAG",imagePath)
//            uploadFile(imagePath)
            c.close()
        }
    }




//    private fun uploadFile(path: String) {
//        val file = File(path)
//        //TODO 修改文件类型
//        val requestBody = RequestBody.create(MediaType.parse("image/jp"), file)
//        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
//        ReportApiService.getProperties().enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
////                Log.i("TAG", "文件上传后的url=" + response.body().getData().getUrl());
//                Log.i("TAG", "文件上传后的url=" + response.body().toString())
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {}
//        })
//    }


}