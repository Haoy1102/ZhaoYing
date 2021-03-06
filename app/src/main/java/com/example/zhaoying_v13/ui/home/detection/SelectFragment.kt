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

        binding.tvfieldFileName.setEndIconOnClickListener {
            selectFile()
        }

        binding.btnUploadFile.setOnClickListener {
            uploadFile()
        }

        viewModel.canPredict.observe(viewLifecycleOwner, Observer { it ->
            if (it == true)
                predictReport()
        })

        binding.btnToResult.setOnClickListener {
            predictReport()
        }

    }

    private fun uploadFile() {
        //??????Loading?????????
        val progressDialog = ProgressDialog(requireContext())
        if (imageState == 1 && binding.courseMenuText.text.toString() != ""
            && viewModel.currentUser.value != null
        ) {
            progressDialog.setTitle("??????")
            progressDialog.setMessage("????????????????????????.....")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.show()
            viewModel.uploadFile(imagePath, binding.courseMenuText.text.toString())
            viewModel.status.observe(viewLifecycleOwner,
                Observer { it ->
                    if (it == "204") {
                        progressDialog.dismiss()
                        Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show()
                        binding.btnUploadFile.visibility = View.INVISIBLE
                        binding.btnToResult.visibility = View.VISIBLE
                        binding.tvUploadFinish.visibility=View.VISIBLE
                        return@Observer
                    }
                    if (it == "408") {
                        progressDialog.dismiss()
                        Toast.makeText(context, "????????????????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                })
        } else if (binding.courseMenuText.text.toString() == "") {
            Toast.makeText(context, "?????????????????????????????????", Toast.LENGTH_SHORT).show()
        } else if (imageState == 0) {
            Toast.makeText(context, "?????????????????????????????????", Toast.LENGTH_SHORT).show()
        } else if (viewModel.currentUser.value == null) {
            Toast.makeText(context, "???????????????????????????", Toast.LENGTH_SHORT).show()
        }

    }

    private fun predictReport() {

        //???????????????
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("??????")
            .setMessage("????????????????????????????????????1????????????????????????????????????")
            .setNegativeButton("??????") { dialog, which ->
                //???????????????Loading?????????
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setTitle("??????")
                progressDialog.setMessage("??????????????????????????????.....")
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progressDialog.show()
                viewModel.getPredicReport(imagePath, binding.courseMenuText.text.toString())
                viewModel.report.observe(viewLifecycleOwner, Observer { it ->
                    if (it.status == "204") {
                        progressDialog.dismiss()
                        val args = Bundle()
                        args.putInt("enterFrom", 0)
                        args.putString("evaluate", it.evaluate)
                        args.putString("url", it.url)
                        args.putString("criterion", it.criterion)
                        requireView().findNavController()
                            .navigate(R.id.action_selectFragment_to_reportFragment, args)
                        Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show()
                    } else if (it.status == "403") {
                        Toast.makeText(context, "?????????????????????????????????", Toast.LENGTH_SHORT).show()
                    } else if (it.status == "408") {
                        Toast.makeText(context, "????????????????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                })

            }
            .setPositiveButton("??????", null)
            .show()
    }

    private fun selectFile() {
        AndPermission.with(this@SelectFragment)
            .runtime()
            .permission(
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE
            )
            .onGranted(object : Action<List<String?>?> {
                override fun onAction(data: List<String?>?) {
                    // ???????????????????????????
                    //????????????
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
        //??????????????????
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
            binding.tvFileName.setText(imagePath)
            Log.i("TAG", imagePath)
            imageState = 1
            c.close()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}