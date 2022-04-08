package com.example.zhaoying_v13.ui.home.detection

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zhaoying_v13.database.CourseInfo
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.database.UserInfo
import com.example.zhaoying_v13.database.UserWithCourses
import com.example.zhaoying_v13.network.ReportApi
import com.example.zhaoying_v13.network.Status
import com.example.zhaoying_v13.ui.myInfo.login.model.UserLoginInfo
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SelectViewModel(
    val database: UserDatabaseDao,
    application: Application
) : ViewModel() {

    private val _courseMenuItem = MutableLiveData<List<String>>()
    val courseMenuItem: LiveData<List<String>> = _courseMenuItem

    private val _currentUser = MutableLiveData<UserInfo?>()
    val currentUser: LiveData<UserInfo?> = _currentUser

    private val _selectedCourse = MutableLiveData<CourseInfo?>()
    val selectedCourse: LiveData<CourseInfo?> = _selectedCourse

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    init {
        initCourseMenu()
    }

    private fun initCourseMenu() {
        viewModelScope.launch {
            //获取当前用户信息
            val currentUser = getCurrentLoginUserInfo()
            //Log.i("SELF_TAG", "getCurrentLoginUserInfo:" + currentUser.toString())
            _currentUser.value = currentUser
            //获取当前用户所选课程list
            if ( _currentUser.value!=null){
                val courselist: List<UserWithCourses> = getUsersWithCourses(currentUser!!.userID)
                //Log.i("SELF_TAG", "getCurrentLoginUserInfo:" + courselist.toString())
                _courseMenuItem.value = courselist[0].courses.map { it.name }
            }
        }
    }

    fun uploadFile(path: String, courseName: String) {
        val file = File(path)
        //val phonenumberBody=RequestBody.create(MediaType.parse("multipart/form-data"), "111")
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val courseNameBody=toRequestBody(courseName)
        val userIDBody=toRequestBody(currentUser.value!!.userID)
        ReportApi.retrofitService.upLoadFiles(
            part, file.name, courseNameBody, userIDBody
        )
            .enqueue(object : Callback<Status> {
                override fun onResponse(call: Call<Status>, response: Response<Status>) {
                    _status.value = response.body()!!.status!!
                    Log.i("TAG", "状态码：" + response.body().toString())
                    Log.i("TAG", "路径：" + file.name)
                }

                override fun onFailure(call: Call<Status>, t: Throwable) {
                    _status.value = "400"
                    Log.i("TAG", "错误信息：" + t.toString())
                }
            })
    }

    private fun toRequestBody( value:String): RequestBody {
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), value);
        return requestBody;
    }

    suspend fun getCurrentLoginUserInfo(): UserInfo? {
        return database.getCurrentLoginUserInfo()
    }

    suspend fun getUsersWithCourses(key: String): List<UserWithCourses> {
        return database.getUsersWithCourses(key)
    }
}