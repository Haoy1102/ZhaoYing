package com.example.zhaoying_v13.ui.login.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.database.UserInfo
import com.example.zhaoying_v13.ui.login.data.LoginRepository
import com.example.zhaoying_v13.ui.login.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    val database: UserDatabaseDao,
    application: Application
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(phonenumber: String, password: String) {
        // can be launched in a separate asynchronous job
        //登陆操作
        val result = loginRepository.login(phonenumber, password)

        //结果成功
        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(result.data.displayName,result.data.userId!!))

            //用户当前状态录入数据库
            if (loginRepository.isLoggedIn==true){
                viewModelScope.launch(Dispatchers.IO) {
                    val key= loginRepository.user!!.userId!!
                    if (database.getUserByID(key)==null){
                        val user=UserInfo(key,result.data.displayName)
                        database.insert(user)
                    }
                    else{//本地保存有用户的信息 更新状态即可
                        Log.i("Database", "设置用户登陆状态")
                        database.setOtherLogin0(key)
                        database.setCurrentLogin1(key)
                        Log.i("Database", "用户登陆状态设置成功")
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


        } else {
            //登陆界面显示提示
            _loginResult.value = LoginResult(error = R.string.login_failed)
            //Log.i("UserLogin",result.toString())
        }
    }



    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

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