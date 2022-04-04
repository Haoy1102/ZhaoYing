package com.example.zhaoying_v13.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.network.ReportApi
import com.example.zhaoying_v13.ui.login.model.LoggedInUser
import com.example.zhaoying_v13.ui.login.model.RegisterUser
import com.example.zhaoying_v13.ui.login.model.UserRegResponse

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class RegisterViewModel(
    val database: UserDatabaseDao,
    application: Application
) : ViewModel() {

    private val _registerUser = MutableLiveData<RegisterUser>()
    val registerUser: LiveData<RegisterUser> = _registerUser

    private val _regStatus = MutableLiveData<String>()
    val regStatus: LiveData<String> = _regStatus

    fun register(userInfo:RegisterUser){
        //val params: Map<String, RequestBody> = HashMap()
        val params= HashMap<String, RequestBody>()
//        val avatar:RequestBody?

//        params.plus(Pair("phone_number",toRequestBody(userInfo.phone_number)))
//        params.plus(Pair("password1",toRequestBody(userInfo.password)))
//        params.plus(Pair("password2",toRequestBody(userInfo.password)))
//        params.plus(Pair("name",toRequestBody(userInfo.displayName)))
//        params.plus(Pair("gender",toRequestBody(userInfo.gender)))

        params["phone_number"] = toRequestBody(userInfo.phone_number)
        params["password1"] = toRequestBody(userInfo.password)
        params["password2"] = toRequestBody(userInfo.password)
        params["name"] = toRequestBody(userInfo.displayName)
        params["gender"] = toRequestBody(userInfo.gender)
//        if (userInfo.birthday!=null)
//        params["birthday"] = toRequestBody(userInfo.birthday)
//        if (userInfo.height!=null)
//            params["height"] = toRequestBody(userInfo.height)
//        if (userInfo.weight!=null)
//            params["weight"] = toRequestBody(userInfo.weight)
//        if (userInfo.idcard_number!=null)
//        params["idcard_number"] = toRequestBody(userInfo.idcard_number)
//        if (userInfo.hobbies!=null)
//        params["hobbies"] = toRequestBody(userInfo.hobbies)

        //头像
//        if (userInfo.avatar!=null)
//         avatar=RequestBody.create(MediaType.parse("multipart/form-data"),userInfo.avatar)
//        else avatar=null

        val displayNameBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), userInfo.displayName)
        val phone_numberBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), userInfo.phone_number)
        val passwordBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), userInfo.password)
        val genderBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), userInfo.gender)


        ReportApi.retrofitService.userRegister(
//            displayNameBody,phone_numberBody,passwordBody,passwordBody,genderBody
        params
        )
            .enqueue(object : Callback<UserRegResponse> {
                override fun onResponse(
                    call: Call<UserRegResponse>,
                    response: Response<UserRegResponse>
                ) {
                    Log.i("SELF_TAG",response.body().toString())
                    Log.i("SELF_TAG","1_regStatus.value=\"200\"")
                    //if (response.body()!!.status=="200"){
                        _regStatus.value="200"
                        Log.i("SELF_TAG","2_regStatus.value=\"200\"")
                        //_registerUser.value=response.body()
//                    }
                    //用户名未填
//                    else if(response.body()!!.status=="A400"){
//                        _regStatus.value="A400"
//                    }
//                    //用户已经注册
//                    else if (response.body()!!.status=="C400"){
//                        _regStatus.value="C400"
//                    }
                }

                override fun onFailure(call: Call<UserRegResponse>, t: Throwable) {
                    //400 网络错误
                    _regStatus.value="400"
                    Log.i("SELF_TAG",t.message.toString())
                }
            })

    }

    private fun toRequestBody( value:String): RequestBody {
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), value);
        return requestBody;
    }




}