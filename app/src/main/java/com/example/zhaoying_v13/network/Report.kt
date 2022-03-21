package com.example.zhaoying_v13.network

import com.squareup.moshi.Json

data class Report(
    val id: String,
    //TODO(02)修改Json_name
    @Json(name="img_src")val imgSrcUrl: String,
    val type: String,
    val price: Double)

