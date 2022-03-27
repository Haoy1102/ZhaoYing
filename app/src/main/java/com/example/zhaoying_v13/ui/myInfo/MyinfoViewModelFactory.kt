package com.example.zhaoying_v13.ui.myInfo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.ui.login.data.LoginDataSource
import com.example.zhaoying_v13.ui.login.data.LoginRepository
import com.example.zhaoying_v13.ui.login.ui.login.LoginViewModel

class MyinfoViewModelFactory (
    private val dataSource: UserDatabaseDao,
                              private val application: Application
): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyinfoViewModel::class.java)) {
            return MyinfoViewModel(
                dataSource, application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}