package com.example.zhaoying_v13.ui.login.model

import com.squareup.moshi.Json
import java.io.File

data class UserRegResponse (
    @Json(name="data")val data: RegisterUser?=null,
    val status:String
)

@Json(name="data")
data class RegisterUser(
    val phone_number:String,
    val password:String,
    val gender:String,
    @Json(name = "name")val displayName:String,//与发送不同命
    val height:String?=null,
    val weight:String?=null,
    val birthday:String?=null,
    val avatar:File?=null,
    val idcard_number:String?=null,
    val hobbies:String?=null,
    //val status:String?=null
)

