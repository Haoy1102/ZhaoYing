package com.example.zhaoying_v13.network


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://110.40.185.43:8000/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface UserLoginApiService {
    @POST("api/user/login")
    @Multipart
    fun userLogin(
        @Part("phone_number")phonenumber:RequestBody,
        @Part("password")password:RequestBody
    ):Call<ResponseBody>
}

object FormdataApi {
    val retrofitService : UserLoginApiService by lazy {
        retrofit.create(UserLoginApiService::class.java)
    }
}