package com.example.zhaoying_v13.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.databinding.RegisterFragmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)

        //选择性别
        val items = listOf("男", "女")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.sexSelectTextField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        //密码检测
        inspectPassword()
        //选择日期 存储在birthday
        selectDate()







        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }


    fun inspectPassword() {
        binding.inspectPasswordText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("DASD","1")
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("DASD","2")
                }
                override fun afterTextChanged(p0: Editable?) {
                    var password:String=binding.passwordText.text.toString()
                    var inspextPassword:String=binding.inspectPasswordText.text.toString()
                    if (password==inspextPassword){
                        binding.inspectPasswordTextField.error =null
                    } else{
                        binding.inspectPasswordTextField.error ="两次密码不一致"
                    }
                }
            }
        )
    }

    fun selectDate(){
        binding.bitrhTextField.setEndIconOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("选择日期")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(parentFragmentManager, "tag")

            datePicker.addOnPositiveButtonClickListener {
                val birthLong = datePicker.selection
                val birthday: String = longToDate(birthLong!!)
                //TODO 使用birthday
                System.out.println(birthday)
                binding.bitrhText.setText(birthday)
            }
        }
    }

    fun longToDate(lo: Long): String {
        val date = Date(lo)
        val sd = SimpleDateFormat("yyyy-MM-dd")
        return sd.format(date)
    }



}



