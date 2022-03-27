package com.example.zhaoying_v13.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="local_user_info_table")
data class UserInfo (
    @PrimaryKey
    var usrID:Long=0L,

    @ColumnInfo(name="phoneNumber")
    val phoneNumber:String,

    @ColumnInfo(name="password")
    val password:String,

    @ColumnInfo(name="lastLoginTime")
    val lastLoginTime: String
        )