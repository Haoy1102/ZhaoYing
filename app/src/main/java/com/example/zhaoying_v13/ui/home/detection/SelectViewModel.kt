package com.example.zhaoying_v13.ui.home.detection

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.database.UserInfo
import com.example.zhaoying_v13.database.UserWithCourses
import com.example.zhaoying_v13.ui.myInfo.login.model.UserLoginInfo
import kotlinx.coroutines.launch

class SelectViewModel(
    val database: UserDatabaseDao,
    application: Application
) : ViewModel() {

    private val _courseMenuItem = MutableLiveData<List<String>>()
    val courseMenuItem: LiveData<List<String>> = _courseMenuItem

    private val _currentUser = MutableLiveData<UserInfo?>()
    val currentUser: LiveData<UserInfo?> = _currentUser

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
            val courselist:List<UserWithCourses> = getUsersWithCourses(currentUser!!.userID)
            //Log.i("SELF_TAG", "getCurrentLoginUserInfo:" + courselist.toString())
            _courseMenuItem.value=courselist[0].courses.map { it.name }
        }
    }

    suspend fun getCurrentLoginUserInfo(): UserInfo? {
        return database.getCurrentLoginUserInfo()
    }

    suspend fun getUsersWithCourses(key: String): List<UserWithCourses> {
        return database.getUsersWithCourses(key)
    }
}