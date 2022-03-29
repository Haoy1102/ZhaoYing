package com.example.zhaoying_v13.ui.login.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    var userId: String?,
    var status:String,      //返回的状态码
    //val logged:Boolean=false,//当前登陆状态
    val displayName:String="匿名用户"
)