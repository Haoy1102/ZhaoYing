package com.example.zhaoying_v13.network

import com.example.zhaoying_v13.ui.myInfo.login.model.UserLoginInfo
import com.example.zhaoying_v13.ui.myInfo.login.model.UserRegResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody


import retrofit2.http.Multipart



//192.168.105.247
//192.168.0.100
//110.40.185.43
private const val BASE_URL = "http://192.168.0.102:8000/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface ReportApiService {
    @GET("realestate")
    fun getProperties():
            Deferred<List<Report>>


    @POST("api/user/login")
    @Multipart
    fun userLogin(
        @Part("phone_number")phonenumber:RequestBody,
        @Part("password")password:RequestBody
        ):Call<UserLoginInfo>


    @POST("api/user/register")
    @Multipart
    fun userRegister(
//        @Part("name") name:RequestBody,
//        @Part("phone_number")phonenumber:RequestBody,
//        @Part("password1")password1:RequestBody,
//        @Part("password2")password2:RequestBody,
//        @Part("gender")gender:RequestBody

        @PartMap params:Map<String, @JvmSuppressWildcards RequestBody>
        //@Part avatar: MultipartBody.Part?,
    ): Call<UserRegResponse>

    @POST("api/user/upload/{filename}")
    @Multipart
    fun upLoadFiles(
        @Part file: MultipartBody.Part?,
        @Path("filename") filename:String
    ): Call<String>?


}

object ReportApi {
    val retrofitService : ReportApiService by lazy {
        retrofit.create(ReportApiService::class.java)
    }
}

