package com.example.zhaoying_v13.ui.home.detection

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zhaoying_v13.databinding.SelectFragmentBinding
import com.example.zhaoying_v13.network.ReportApi
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SelectFragment : Fragment() {

    private lateinit var viewModel: SelectViewModel
    private var _binding: SelectFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var imagePath:String
    private var imageState=0
    companion object {
        fun newInstance() = SelectFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SelectFragmentBinding.inflate(inflater, container, false)

        val btnSelectFile = binding.btnSelectFile
        btnSelectFile.setOnClickListener {
            Log.i("Tag", "按钮有效")
            AndPermission.with(this@SelectFragment)
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
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
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

        binding.btnUploadFile.setOnClickListener {
            if (imageState==1){
                Log.i("Tag", "上传按钮有效")
                uploadFile(imagePath)
            }
            else{
                Log.i("Tag", "上传按钮退出")
                Toast.makeText(context,"选择文件后才可以上传噢",Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
            val c: Cursor =
                selectedImage?.let {
                    requireActivity().contentResolver.query(
                        it,
                        filePathColumns,
                        null,
                        null,
                        null
                    )
                }!!
            c.moveToFirst()
            val columnIndex = c.getColumnIndex(filePathColumns[0])
            imagePath = c.getString(columnIndex)


//            binding.textField.
            binding.inputTextFiled.setText(imagePath)
            Log.i("TAG", imagePath)
            imageState=1
            c.close()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uploadFile(path: String) {
        val file = File(path)
        //TODO 修改文件类型
        //val phonenumberBody=RequestBody.create(MediaType.parse("multipart/form-data"), "111")
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        ReportApi.retrofitService.upLoadFiles(part,file.name)?.enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("TAG", "状态码：" + response.body().toString())
                Log.i("TAG","路径："+file.name)
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("TAG", "错误信息："+t.toString() )
            }
        })
    }



}