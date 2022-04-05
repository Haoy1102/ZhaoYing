package com.example.zhaoying_v13.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.databinding.FragmentLoginBinding
import com.example.zhaoying_v13.ui.login.model.UserLoginInfo


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //数据库初始化处理
        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = LoginViewModelFactory(dataSource, application)
        loginViewModel = ViewModelProvider(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.phoneNumber
        val passwordEditText = binding.password
        val loginButton = binding.loginButton
        val loadingProgressBar = binding.loading
        val registerText=binding.textRegister

//        loginViewModel.loginFormState.observe(viewLifecycleOwner,
//            Observer { loginFormState ->
//                if (loginFormState == null) {
//                    return@Observer
//                }
//                loginButton.isEnabled = loginFormState.isDataValid
//                loginFormState.usernameError?.let {
//                    usernameEditText.error = getString(it)
//                }
//                loginFormState.passwordError?.let {
//                    passwordEditText.error = getString(it)
//                }
//            })

        loginViewModel.loggedInUser.observe(viewLifecycleOwner,
            Observer { loggedInUser ->
                loggedInUser ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                if (loggedInUser.status=="200"){
                    Log.i("SLEF_TAG","loggedInUser.status==\"200\"")
                    updateUiWithUser(loggedInUser)
                    updateDatabaseWithUser(loggedInUser)
                    requireActivity().finish()
                }
                if (loggedInUser.status=="B404")
                    showLoginFailed("密码不正确")
                if (loggedInUser.status=="A404")
                    showLoginFailed("账户不存在")
            })

//        val afterTextChangedListener = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                // ignore
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                // ignore
//            }
//            override fun afterTextChanged(s: Editable) {
//                loginViewModel.loginDataChanged(
//                    usernameEditText.text.toString(),
//                    passwordEditText.text.toString()
//                )
//            }
//        }
//        usernameEditText.addTextChangedListener(afterTextChangedListener)
//        passwordEditText.addTextChangedListener(afterTextChangedListener)
//        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                loginViewModel.login(
//                    usernameEditText.text.toString(),
//                    passwordEditText.text.toString()
//                )
//            }
//            false
//        }

        loginButton.setOnClickListener {
            //loadingBar显示
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
        //跳转注册界面
        binding.textRegister.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
        )


    }

    private fun updateUiWithUser(loggedInUser: UserLoginInfo) {
        val welcome = getString(R.string.welcome) + loggedInUser.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun updateDatabaseWithUser(loggedInUser: UserLoginInfo){
        loginViewModel.updateDatabaseWithUser(loggedInUser)
    }

    private fun showLoginFailed(errorString: String) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}