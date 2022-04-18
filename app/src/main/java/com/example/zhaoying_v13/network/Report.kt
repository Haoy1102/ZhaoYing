package com.example.zhaoying_v13.network

import com.squareup.moshi.Json

data class Report(
    val status: String,
    val evaluate: String? = null,
    val url: String? = null,
)

