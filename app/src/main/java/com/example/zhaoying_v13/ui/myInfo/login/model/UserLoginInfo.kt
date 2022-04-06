package com.example.zhaoying_v13.ui.myInfo.login.model;

import com.squareup.moshi.Json

data class UserLoginInfo(
    val status: String,
    @Json(name = "id") val userId: String? = null,
    @Json(name = "name") val displayName: String? = "匿名用户"
)
