package com.example.zhaoying_v13.ui.myInfo.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.databinding.RegisterFragmentBinding
import com.example.zhaoying_v13.ui.myInfo.login.model.RegisterUser
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var registerViewModel: RegisterViewModel
    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //数据库初始化处理
        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = RegisterViewModelFactory(dataSource, application)
        registerViewModel = ViewModelProvider(this, viewModelFactory)
            .get(RegisterViewModel::class.java)


        registerViewModel.registerUser.observe(viewLifecycleOwner,
            Observer { registerUser ->
                registerUser ?: return@Observer
                binding.loading.visibility = View.GONE
                if (registerUser.status == "200") {
                    Log.i("SELF_TAG", "loggedInUser.status==\"200\"")
                    updateUiWithUser(registerViewModel.registerUser.value!!)
                    updateDatabaseWithUser(registerUser)
                    requireActivity().finish()
                }
                if (registerUser.status == "A400")
                    showLoginFailed("用户名未填")
                if (registerUser.status == "B400")
                    showLoginFailed("两次密码输入不同")
                if (registerUser.status == "C400")
                    showLoginFailed("该手机号已被注册")
                if (registerUser.status=="408"){
                    showLoginFailed("网络错误，请检查网络连接")
                }
            })


        binding.regiButton.setOnClickListener {
            if (inputCorrectChecked()&&inputEmptyChecked()) {
                //loadingBar显示
                binding.loading.visibility = View.VISIBLE
                val userInfo = RegisterUser(
                    null,
                    binding.phoneNumberText.text.toString(),
                    binding.passwordText.text.toString(),
                    binding.displayNameText.text.toString(),
                    binding.sexSelected.text.toString(),
                    binding.heightText.text.toString(),
                    binding.weightText.text.toString(),
                    binding.bitrhText.text.toString(),
                    null, null, null
                )
                registerViewModel.register(userInfo)
            }
        }

    }

private fun init(){
    //选择性别
    val items = listOf("男", "女")
    val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
    (binding.sexSelectTextField.editText as? AutoCompleteTextView)?.setAdapter(adapter)

    //密码检测
    inspectPassword()
    //选择日期 存储在birthday
    selectDate()

}

    private fun updateUiWithUser(registerUser: RegisterUser) {
        val welcome = getString(R.string.welcome) + registerUser.displayName
        Toast.makeText(requireContext(), welcome, Toast.LENGTH_LONG).show()
    }

    private fun updateDatabaseWithUser(registerUser: RegisterUser) {
        registerViewModel.updateDatabaseWithUser(registerUser)
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_LONG).show()
    }

    fun inspectPassword() {
        binding.inspectPasswordText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("DASD", "1")
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("DASD", "2")
                }

                override fun afterTextChanged(p0: Editable?) {
                    var password: String = binding.passwordText.text.toString()
                    var inspextPassword: String = binding.inspectPasswordText.text.toString()
                    if (password == inspextPassword) {
                        binding.inspectPasswordTextField.error = null
                    } else {
                        binding.inspectPasswordTextField.error = "两次密码不一致"
                    }
                }
            }
        )
    }

    fun selectDate() {
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

    private fun inputEmptyChecked(): Boolean {
        var status = true;
        if (binding.displayNameText.text.toString() == "") {
            status = false
            binding.displayNameTextField.error = "请输入昵称"
        }else{
            binding.displayNameTextField.error = null
        }

        if (binding.phoneNumberText.text.toString() == "") {
            status = false
            binding.phoneNumberTextField.error = "请输入手机号码"
        }else{
            binding.phoneNumberTextField.error = null
        }

        if (binding.passwordText.text.toString() == "") {
            status = false
            binding.passwordTextField.error = "请输入密码"
        }else{
            binding.passwordTextField.error = null
        }
        if (binding.sexSelected.text.toString() == "") {
            status = false
            binding.sexSelectTextField.error = "请选择性别"
        }else{
            binding.sexSelectTextField.error = null
        }
        return status
    }

    private fun inputCorrectChecked(): Boolean {
        var status = true;
        if (binding.phoneNumberText.text.toString().length != 11) {
            status = false
            binding.phoneNumberTextField.error = "手机号码尾数不正确"
        }else{
            binding.phoneNumberTextField.error = null
        }
        if (binding.passwordText.text.toString().length < 6) {
            status = false
            binding.passwordTextField.error = "密码小于6位"
        }else{
            binding.passwordTextField.error = null
        }
        return status
    }

}



