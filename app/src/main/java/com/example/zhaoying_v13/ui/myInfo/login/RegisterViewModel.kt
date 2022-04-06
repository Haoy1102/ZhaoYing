package com.example.zhaoying_v13.ui.myInfo.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.database.UserInfo
import com.example.zhaoying_v13.network.ReportApi
import com.example.zhaoying_v13.ui.myInfo.login.model.RegisterUser
import com.example.zhaoying_v13.ui.myInfo.login.model.UserRegResponse
import kotlinx.coroutines.launch

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
    val database: UserDatabaseDao,
    application: Application
) : ViewModel() {

    private val _registerUser = MutableLiveData<RegisterUser>()
    val registerUser: LiveData<RegisterUser> = _registerUser

//    private val _regStatus = MutableLiveData<String>()
//    val regStatus: LiveData<String> = _regStatus

    fun register(userInfo:RegisterUser){
        val params= HashMap<String, RequestBody>()

        params["phone_number"] = toRequestBody(userInfo.phone_number!!)
        params["password1"] = toRequestBody(userInfo.password!!)
        params["password2"] = toRequestBody(userInfo.password!!)
        params["name"] = toRequestBody(userInfo.displayName!!)
        params["gender"] = toRequestBody(userInfo.gender!!)
        if (userInfo.birthday!=null)
        params["birthday"] = toRequestBody(userInfo.birthday)
        if (userInfo.height!=null)
            params["height"] = toRequestBody(userInfo.height)
        if (userInfo.weight!=null)
            params["weight"] = toRequestBody(userInfo.weight)
        if (userInfo.idcard_number!=null)
        params["idcard_number"] = toRequestBody(userInfo.idcard_number)
        if (userInfo.hobbies!=null)
        params["hobbies"] = toRequestBody(userInfo.hobbies)

        //头像
//        if (userInfo.avatar!=null)
//         avatar=RequestBody.create(MediaType.parse("multipart/form-data"),userInfo.avatar)
//        else avatar=null

        ReportApi.retrofitService.userRegister(params)
            .enqueue(object : Callback<UserRegResponse> {
                override fun onResponse(
                    call: Call<UserRegResponse>,
                    response: Response<UserRegResponse>
                ) {
                    Log.i("SELF_TAG",response.body().toString())
                    if (response.body()!!.status=="200"){
                        Log.i("SELF_TAG","regStatus.value=\"200\"")
                        _registerUser.value=RegisterUser(
                            response.body()!!.status,
                            response.body()!!.phone_number!!,
                            response.body()!!.password!!,
                            response.body()!!.displayName!!,
                            response.body()!!.gender!!,
                            response.body()!!.height,
                            response.body()!!.weight,
                            response.body()!!.birthday,
                            response.body()!!.hobbies,
                            response.body()!!.idcard_number,
                            null,response.body()!!.id
                        )
                    }
                    //用户名未填
                    else if(response.body()!!.status=="A400"){
                        _registerUser.value=RegisterUser(response.body()!!.status)
                    }
                    //用户已经注册
                    else if (response.body()!!.status=="C400"){
                        _registerUser.value=RegisterUser(response.body()!!.status)
                    }
                }
                override fun onFailure(call: Call<UserRegResponse>, t: Throwable) {
                    //400 网络错误
                    _registerUser.value=RegisterUser("400")
                    Log.i("SELF_TAG",t.message.toString())
                }
            })
    }

    private fun toRequestBody( value:String): RequestBody {
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), value);
        return requestBody;
    }

    fun updateDatabaseWithUser(registerUser: RegisterUser) {
        viewModelScope.launch {
            val key = registerUser.userId!!
            //本地没有信息，录入
            if (getUserByID(key) == null) {
                Log.i("SELF_TAG", "Database:本地没有信息，录入")
                val user = UserInfo(key, registerUser.displayName!!)
                database.setOtherLogin0(key)
                database.insert(user)
            } else {//本地保存有用户的信息 更新状态即可
                Log.i("SELF_TAG", "Database:本地保存有用户的信息 更新状态即可")
                database.setOtherLogin0(key)
                database.setCurrentLogin1(key)
                Log.i("SELF_TAG", "Database:用户登陆状态设置成功")
            }
        }
    }

    suspend fun getUserByID(key: String): UserInfo? {
        return database.getUserByID(key)
    }

}