package com.example.zhaoying_v13.ui.myInfo.login.model

import com.squareup.moshi.Json

data class UserRegResponse (
    val status:String,
    val phone_number:String?,
    val password:String?,
    @Json(name = "name")val displayName:String?,//与发送不同命
    val gender:String?,
    val height:String?=null,
    val weight:String?=null,
    val birthday:String?=null,
    val hobbies:String?=null,
    val idcard_number:String?=null,
    val id:String?=null
    //val avatar_status:String?=null,
    //val avatar:File?=null,
)


