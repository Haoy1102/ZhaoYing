package com.example.zhaoying_v13.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserJson (
    val phonenumber:String,
    val passwd:String
        )