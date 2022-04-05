package com.example.zhaoying_v13.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.database.UserInfo
import com.example.zhaoying_v13.network.ReportApi
import com.example.zhaoying_v13.ui.login.model.UserLoginInfo
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginViewModel(
    val database: UserDatabaseDao,
    application: Application
) : ViewModel() {

//    private val _loginForm = MutableLiveData<LoginFormState>()
//    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loggedInUser = MutableLiveData<UserLoginInfo>()
    val loggedInUser: LiveData<UserLoginInfo> = _loggedInUser

//    private val _loginResult = MutableLiveData<LoginResult>()
//    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(phonenumber: String, password: String) {
        // can be launched in a separate asynchronous job
        //val result = loginRepository.login(phonenumber, password)
        //登陆操作

        // TODO: handle loggedInUser authentication 发送网络请求
        val phonenumberBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), phonenumber)
        val passwordBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), password)

        //网络请求
        try {
            ReportApi.retrofitService.userLogin(phonenumberBody, passwordBody)
                .enqueue(object : Callback<UserLoginInfo> {
                    override fun onResponse(
                        call: Call<UserLoginInfo>,
                        info: Response<UserLoginInfo>
                    ) {//登陆成功
                        if (info.body()!!.status == "200") {
                            Log.i(
                                "SELF_TAG",
                                "success:" + info.body()?.userId + "   " + info.body()!!.status
                            )
                            //设置ID 网络状态码和登陆状态
                            _loggedInUser.value = UserLoginInfo("200",
                                info.body()!!.userId!!,
                                info.body()!!.displayName)
//                        _loggedInUser.value!!.userId = response.body()!!.id!!
//                        _loggedInUser.value!!.status = "200"
                            Log.i("SELF_TAG", "status:" + _loggedInUser.value!!.status)
                        } else if (info.body()!!.status == "B404") {
                            //密码不正确
                            _loggedInUser.value = UserLoginInfo("B404")
                            Log.i("SELF_TAG", "密码不正确:" + info.body()!!.status)
                        } else {
                            //账户不存在
                            _loggedInUser.value = UserLoginInfo("A404")
                            Log.i("SELF_TAG", "账户不存在:" + info.body()!!.status)
                        }
                    }

                    override fun onFailure(call: Call<UserLoginInfo>, t: Throwable) {
                        Log.i("SELF_TAG", "onFailure:" + t.message)
                    }
                })
        }catch (e:Exception){
            Log.i("SELF_TAG",e.message.toString())
        }



        //return Result.Error(data = _loggedInUser.value!!)
        //Default设置


//        //结果成功
//        if (result is Result.Success) {
//            _loginResult.value =
//                LoginResult(
//                    success = LoggedInUserView(
//                        result.data.displayName,
//                        result.data.userId!!
//                    )
//                )
//        } else {
//            //登陆界面显示提示
//            _loginResult.value = LoginResult(error = R.string.login_failed)
//            //Log.i("UserLogin",result.toString())
//        }
    }

    fun updateDatabaseWithUser(loggedInUser: UserLoginInfo) {
        viewModelScope.launch {
            val key = loggedInUser.userId!!
            //本地没有信息，录入
            if (getUserByID(key) == null) {
                Log.i("SLEF_TAG", "Database:本地没有信息，录入")
                val user = UserInfo(key, loggedInUser.displayName!!)
                database.setOtherLogin0(key)
                database.insert(user)
            } else {//本地保存有用户的信息 更新状态即可
                Log.i("SLEF_TAG", "Database:本地保存有用户的信息 更新状态即可")
                database.setOtherLogin0(key)
                database.setCurrentLogin1(key)
                Log.i("SLEF_TAG", "Database:用户登陆状态设置成功")
            }
//                    Log.i("Database查询1:", database.getCurrent().toString())
//                    val userList:List<UserInfo?>?
//                userList=database.getAllUser().value
//                if (userList != null) {
//                    for (user in userList)
//                        Log.i("Database全部用户:",user.toString() )
//                }


        }
    }
        suspend fun getUserByID(key: String): UserInfo? {
            return database.getUserByID(key)
        }

//        fun loginDataChanged(username: String, password: String) {
//            if (!isUserNameValid(username)) {
//                _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
//            } else {
//                _loginForm.value = LoginFormState(isDataValid = true)
//            }
//        }

        // A placeholder username validation check
        private fun isUserNameValid(username: String): Boolean {
            return if (username.contains("@")) {
                Patterns.EMAIL_ADDRESS.matcher(username).matches()
            } else {
                username.isNotBlank()
            }
        }

        // A placeholder password validation check
        private fun isPasswordValid(password: String): Boolean {
            return password.length > 5
        }
    }