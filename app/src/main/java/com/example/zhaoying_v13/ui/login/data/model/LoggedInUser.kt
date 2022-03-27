package com.example.zhaoying_v13.ui.login.data.model

import androidx.room.ColumnInfo
import java.util.*

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(

    val userId: String,
    val status:Int,
    val displayName:String="匿名用户"
)