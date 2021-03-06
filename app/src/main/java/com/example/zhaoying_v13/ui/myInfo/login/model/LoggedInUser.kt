package com.example.zhaoying_v13.ui.myInfo.login.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser1(
    val status:String,      //返回的状态码
    val userId: String?=null,
    //val logged:Boolean=false,//当前登陆状态
    val displayName:String?
)