package com.example.zhaoying_v13.ui.home.detection;

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zhaoying_v13.database.UserDatabaseDao

class SelCourseViewModelFactory(
    private val dataSource: UserDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelCourseViewModel::class.java)) {
            return SelCourseViewModel(
                dataSource, application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
