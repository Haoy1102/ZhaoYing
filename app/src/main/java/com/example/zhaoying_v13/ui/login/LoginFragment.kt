package com.example.zhaoying_v13.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.databinding.LoginFragmentBinding
import com.example.zhaoying_v13.network.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)


        //注册按钮
        binding.textRegister.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
        )

        //登录设置
        binding.loginButton.setOnClickListener {
            val user = User(
                binding.phoneNumberText.text.toString(),
                binding.passwordText.text.toString()
            )
            val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(User::class.java)
            val json = jsonAdapter.toJson(user)
            println(json)
            val body =
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

            UserLoginApi.retrofitService.postUserLogin(body)
                .enqueue(object : Callback<State> {
                    override fun onResponse(call: Call<State>, response: Response<State>) {
                        System.out.println(response.body()!!.id)
                    }

                    override fun onFailure(call: Call<State>, t: Throwable) {
                        System.out.println("Failure: " + t.message)
                    }
                })

        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}