package com.example.zhaoying_v13.ui.home.detection

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.databinding.SelectFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class SelectFragment : Fragment() {

    private lateinit var viewModel: SelectViewModel
    private var _binding: SelectFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var imagePath: String
    private var imageState = 0



    companion object {
        fun newInstance() = SelectFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SelectFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = SelectViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SelectViewModel::class.java)



        initCourseMenu()
        selectFile()
        uploadFile()




        binding.testButton.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_selectFragment_to_reportFragment)

        }



    }

    private fun uploadFile(){
        //上传Loading对话框
        val progressDialog=ProgressDialog(requireContext())
        binding.btnUploadFile.setOnClickListener {
            Log.i("SELF_TAG", binding.courseMenuText.text.toString())
            //Log.i("SELF_TAG", binding.courseMenu.hint.toString())

            if (imageState == 1 && binding.courseMenuText.text.toString() != ""
                && viewModel.currentUser.value != null
            ) {
                progressDialog.setTitle("提示")
                progressDialog.setMessage("正在上传，请稍后.....")
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progressDialog.show()
                viewModel.uploadFile(imagePath, binding.courseMenuText.text.toString())
                viewModel.status.observe(viewLifecycleOwner,
                    Observer { it->
                        if (it=="204"){
                            progressDialog.dismiss()
                            Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show()
                            predictReport()
                        }
                    })
            } else if (binding.courseMenuText.text.toString()=="") {
                Toast.makeText(context, "选择课程后才可以上传噢", Toast.LENGTH_SHORT).show()
            } else if (imageState == 0) {
                Toast.makeText(context, "选择文件后才可以上传噢", Toast.LENGTH_SHORT).show()
            } else if (viewModel.currentUser.value == null) {
                Toast.makeText(context, "登录后才可以上传噢", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun predictReport(){
        //上传后弹窗
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("确认")
            .setMessage("立即查看结果可能需要等待1分钟，点击确定以立即查看")
            .setNegativeButton("确定") { dialog, which ->
                //云端处理中Loading对话框
                val progressDialog=ProgressDialog(requireContext())
                progressDialog.setTitle("提示")
                progressDialog.setMessage("云端正在处理，请稍后.....")
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progressDialog.show()
                viewModel.getPredicReport(imagePath,binding.courseMenuText.text.toString())
                viewModel.report.observe(viewLifecycleOwner, Observer { it->
                    if (it.status=="204"){
                        progressDialog.dismiss()
                        //TODO 设置Bundle
                        val args = Bundle()
                        args.putInt("enterFrom",0)
                        args.putString("evaluate", it.evaluate)
                        args.putString("url",it.url)
                        requireView().findNavController().navigate(R.id.action_selectFragment_to_reportFragment,args)
                        Toast.makeText(context,"结果分析成功",Toast.LENGTH_SHORT).show()
                    }
                    else if (it.status=="403"){
                        Toast.makeText(context,"分析错误，请练习管理员",Toast.LENGTH_SHORT).show()
                    }
                })

            }
            .setPositiveButton("取消",null)
            .show()
    }

    private fun selectFile() {
        binding.btnSelectFile.setOnClickListener {
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

                    }
                })
                .start()
        }
    }

//    fun uploadFile(path: String, courseName: String) {
//        val file = File(path)
//        //val phonenumberBody=RequestBody.create(MediaType.parse("multipart/form-data"), "111")
//        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
//        ReportApi.retrofitService.upLoadFiles(
//            part, "test2.mp4", courseName, "120"
//        )
//            .enqueue(object : Callback<String> {
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    //_status.value = response.body()
//
//                    Log.i("TAG", "状态码：" + response.body().toString())
//                    Log.i("TAG", "路径：" + file.name)
//                }
//
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    //_status.value = "400"
//                    Log.i("TAG", "错误信息：" + t.toString())
//                }
//            })
//    }

    private fun initCourseMenu() {
        viewModel.courseMenuItem.observe(viewLifecycleOwner,
            Observer { it ->
                if (it != null) {
                    val items = viewModel.courseMenuItem.value
                    val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items!!)
                    (binding.courseMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
            })
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
            binding.inputTextFiled.setText(imagePath)
            Log.i("TAG", imagePath)
            imageState = 1
            c.close()
        }
    }

//    private fun uploadFile(path: String) {
//        val file = File(path)
//        //TODO 修改文件类型
//        //val phonenumberBody=RequestBody.create(MediaType.parse("multipart/form-data"), "111")
//        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
//        ReportApi.retrofitService.upLoadFiles(part, file.name)?.enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                Log.i("TAG", "状态码：" + response.body().toString())
//                Log.i("TAG", "路径：" + file.name)
//            }
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.i("TAG", "错误信息：" + t.toString())
//            }
//        })
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}