package com.example.zhaoying_v13.ui.login.model

import com.squareup.moshi.Json
import java.io.File
import java.sql.ClientInfoStatus

data class UserRegResponse (
    val status:String,
    val phone_number:String,
    val password:String,
    val gender:String,
    @Json(name = "name")val displayName:String,//与发送不同命
    val height:String?=null,
    val weight:String?=null,
    val birthday:String?=null,
    val hobbies:String?=null,
    val idcard_number:String?=null,
    //val avatar_status:String?=null,
    //val avatar:File?=null,



)


