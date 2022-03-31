package com.example.zhaoying_v13.ui.login.model

import java.io.File

data class RegisterUser1 (
    val phone_number:String,
    val password1:String,
    val password2:String,
    val displayName:String,//与发送不同命
    val gender:String,
    val height:String?=null,
    val weight:String?=null,
    val avatar:File?=null,
    val birthday:String?=null,
    val idcard_number:String?=null,
    val hobbies:String?=null
        )