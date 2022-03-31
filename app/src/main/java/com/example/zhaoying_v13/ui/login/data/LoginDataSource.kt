package com.example.zhaoying_v13.ui.login.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.zhaoying_v13.network.ReportApi
import com.example.zhaoying_v13.network.UserLoginResponse
import com.example.zhaoying_v13.ui.login.data.model.LoggedInUser
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import okhttp3.RequestBody
import java.util.concurrent.locks.ReentrantLock


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    private var loggedInUser= MutableLiveData<LoggedInUser>()

//    fun login(phonenumber: String, password: String): Result<LoggedInUser> {
//        //初始化接收对象 400为badrequest
//        //var loggedInUser = LoggedInUser(null, "400")
//
//        try {
//            // TODO: handle loggedInUser authentication 发送网络请求
//            val phonenumberBody: RequestBody =
//                RequestBody.create(MediaType.parse("multipart/form-data"), phonenumber)
//            val passwordBody: RequestBody =
//                RequestBody.create(MediaType.parse("multipart/form-data"), password)
//
//            //网络请求
//
//                ReportApi.retrofitService.userLogin(phonenumberBody, passwordBody)
//                    .enqueue(object : Callback<UserLoginResponse> {
//                        override fun onResponse(
//                            call: Call<UserLoginResponse>,
//                            response: Response<UserLoginResponse>
//                        ) {//登陆成功
//                            if (response.body()!!.status == "200") {
//                                Log.i(
//                                    "TAGLogin",
//                                    "success:" + response.body()?.id + "   " + response.body()!!.status
//                                )
//                                //设置ID 网络状态码和登陆状态
//                                condition.signalAll()
//                                loggedInUser.userId = response.body()!!.id!!
//                                loggedInUser.status = "200"
//                                Log.i("TAGLogin", "status:" + loggedInUser.status)
//                            } else if (response.body()!!.status == "B404") {
//                                //密码不正确
//                                condition.signalAll()
//                                loggedInUser.status = "B404"
//                                Log.i("TAGLogin", "密码不正确:" + response.body()!!.status)
//                            } else {
//                                //账户不存在
//                                condition.signalAll()
//                                loggedInUser.status = "A404"
//                                Log.i("TAGLogin", "账户不存在:" + response.body()!!.status)
//                            }
//                        }
//                        override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
//                            condition.signal()
//                            Log.i("TAGLogin", "onFailure:" + t.message)
//                        }
//                    })
//                condition.await()     // like wait()
//                if (loggedInUser.status == "200") {
//                    Log.i("TAGLogin", "status==200返回Success")
//                    return Result.Success(data = loggedInUser)
//                }
//                //有状态码的错误返回
//                Log.i("TAGLogin", "loginerror")
//                //return Result.Error(data = loggedInUser)
//
//
//
//            //Default设置
//        } catch (e: Throwable) {
//            Log.i("UserLogin", e.message.toString())
//            //没有状态码的返回 400 badRequest错误
//            return Result.Error(loggedInUser)
//        }
//
//        loggedInUser.observe(
//
//        )
//    }

    fun logout() {
        // TODO: revoke authentication
    }
}