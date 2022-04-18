package com.example.zhaoying_v13.ui.myInfo

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.zhaoying_v13.database.CourseInfo
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.database.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyinfoViewModel(
    val database: UserDatabaseDao,
    application: Application
) : ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

//    private val _report = MutableLiveData<Report>()
//
//    val report: LiveData<Report>
//        get() = _report


    private val _currentUser = MutableLiveData<UserInfo?>()
    val currentUser: LiveData<UserInfo?> = _currentUser

//    private val allLoginUser=database.getAllUser()

    //private var currentUserNum = database.getCurrentLoginUserNum()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        updateUserState()
    }

    fun updateUserState() {
        viewModelScope.launch {
            val currentUser = getCurrentLoginUserInfo()
            Log.i("Database", "getCurrentLoginUserInfo:" + currentUser.toString())
            _currentUser.value = currentUser
//            database.insertCourse(CourseInfo())
        }
    }

    fun userLogout() {
        viewModelScope.launch {
            setAllLogout()
        }
    }


    suspend fun getCurrentLoginUserInfo(): UserInfo? {
        return database.getCurrentLoginUserInfo()
    }

    suspend fun setAllLogout() {
        database.setAllLogout()
    }


//     private fun getCurrentUser() {
//         val loginedUerList=allLoginUser.value
//         Log.i("Database", "2:运行到ViewModel")
//             _currentUser.value= loginedUerList?.get(0)
//         Log.i("Database", "3:getCurrentUser()执行结果："+_currentUser.value.toString())

    //Log.i("Database", ":getCurren()执行结果："+database.getCurrent().value.toString())


//         Thread {
//             _currentUser.postValue(database.getCurrentLoginUserInfo())
//             Log.i("Database", "3:curre ntUser:"+currentUser.value.toString())
//         }.start()
//        viewModelScope.launch {
//            //val currentUserNum = database.getCurrentLoginUserNum()
//
//            //Log.i("Database", "2:getCurrentLoginUserNum():" + currentUserNum)
//            //if (currentUserNum > 0) {
//                val currentUser= database.getCurrentLoginUserInfo()
//
//            Log.i("Database", "3:currentUser:"+currentUser.value.toString())
//                //Log.i("Database", "3:getCurrentLoginUserInfo():" + currentUser.toString())
//                return@launch
//            //}
//        }
//    }


//    private fun getReports() {
//        coroutineScope.launch {
//            var getReportDefferd = ReportApi.retrofitService.getProperties()
//            var listResult = getReportDefferd.await()
//            try {
//                if (listResult.size > 0) {
//                    _report.value = listResult[0]
//                }
//            } catch (e: Exception) {
//                _status.value = "Failure: ${e.message}"
//            }
//        }
//    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}