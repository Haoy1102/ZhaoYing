package com.example.zhaoying_v13.ui.myInfo

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.database.UserInfo
import com.example.zhaoying_v13.network.Report
import com.example.zhaoying_v13.network.ReportApi
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

    private val _report = MutableLiveData<Report>()

    val report: LiveData<Report>
        get() = _report


    private val _currentUser = MutableLiveData<UserInfo?>()
    val currentUser: LiveData<UserInfo?> = _currentUser

//    private val allLoginUser=database.getAllUser()

    //private var currentUserNum = database.getCurrentLoginUserNum()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
       //getReports()
//        getCurrentUser()
        initUserState()
    }

    fun initUserState(){
        viewModelScope.launch {
            val userList=getAllUser()
            Log.i("Database","AllUser:"+userList[0].toString())
            _currentUser.value=userList[0]
        }
    }

    suspend fun getAllUser():List<UserInfo?>{
        val userList=database.getAllUser()
        return userList
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




    private fun getReports() {
        coroutineScope.launch {
            var getReportDefferd = ReportApi.retrofitService.getProperties()
            var listResult = getReportDefferd.await()
            try {
                if (listResult.size > 0) {
                    _report.value = listResult[0]
                }
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}