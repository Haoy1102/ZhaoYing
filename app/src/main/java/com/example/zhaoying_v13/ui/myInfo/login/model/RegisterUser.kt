package com.example.zhaoying_v13.ui.myInfo.login.model

import java.io.File

data class RegisterUser (
    val status:String?=null,
    val phone_number:String?=null,
    val password:String?=null,
    val displayName:String?=null,//与发送不同命
    val gender:String?=null,
    val height:String?=null,
    val weight:String?=null,
    val birthday:String?=null,
    val hobbies:String?=null,
    val idcard_number:String?=null,
    val avatar:File?=null,
    val userId:String?=null
        )