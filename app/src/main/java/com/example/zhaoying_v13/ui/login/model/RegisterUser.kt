package com.example.zhaoying_v13.ui.login.model

import java.io.File

data class RegisterUser (
    val phone_number:String,
    val password:String,
    val displayName:String,//与发送不同命
    val gender:String,
    val height:String?=null,
    val weight:String?=null,
    val birthday:String?=null,
    val hobbies:String?=null,
    val idcard_number:String?=null,
    val avatar:File?=null
        )