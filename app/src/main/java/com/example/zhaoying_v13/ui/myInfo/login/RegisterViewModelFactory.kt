package com.example.zhaoying_v13.ui.myInfo.login;

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zhaoying_v13.database.UserDatabaseDao

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class RegisterViewModelFactory(
    private val dataSource: UserDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                dataSource, application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}