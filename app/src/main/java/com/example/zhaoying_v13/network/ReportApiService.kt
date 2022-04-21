package com.example.zhaoying_v13.network


import com.example.zhaoying_v13.ui.myInfo.login.model.UserLoginInfo
import com.example.zhaoying_v13.ui.myInfo.login.model.UserRegResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


//192.168.105.247
//192.168.0.100
//110.40.185.43
//10.135.43.31
//202.196.117.145
//192.168.109.247


private const val BASE_URL = "http://192.168.109.247:8000/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
val httpClient = OkHttpClient.Builder()
    .callTimeout(2, TimeUnit.MINUTES)
    .connectTimeout(120, TimeUnit.SECONDS)
    .readTimeout(120, TimeUnit.SECONDS)
    .writeTimeout(120, TimeUnit.SECONDS)

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(httpClient.build())
    .build()

interface ReportApiService {
//    @GET("realestate")
//    fun getProperties():
//            Deferred<List<Report>>


    @POST("api/user/login")
    @Multipart
    fun userLogin(
        @Part("phone_number")phonenumber:RequestBody,
        @Part("password")password:RequestBody
        ):Call<UserLoginInfo>


    @POST("api/user/register")
    @Multipart
    fun userRegister(
        @PartMap params:Map<String, @JvmSuppressWildcards RequestBody>
        //@Part avatar: MultipartBody.Part?,
    ): Call<UserRegResponse>

    @POST("api/user/upload/{filename}")
    @Multipart
    fun upLoadFiles(
        @Part file: MultipartBody.Part?,
        @Path("filename") filename:String,
        @Part("course")courseName:RequestBody,
        @Part("userID")userID:RequestBody
    ): Call<Status>

    @POST("api/user/predict")
    @Multipart
    fun getPredicReport(
        @Part("userID")userID:RequestBody,
        @Part("course")courseName:RequestBody,
        @Part("filename")filename:RequestBody
    ): Call<Report>

}

object ReportApi {
    val retrofitService : ReportApiService by lazy {
        retrofit.create(ReportApiService::class.java)
    }
    val baseURL=BASE_URL
}

