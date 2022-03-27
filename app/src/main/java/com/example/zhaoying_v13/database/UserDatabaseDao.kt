package com.example.zhaoying_v13.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query
import androidx.room.Update

@Dao
public interface UserDatabaseDao {
    @Insert
    fun insert(user:UserInfo)

    @Update
    fun update(user:UserInfo)

    @Query("SELECT * from local_user_info_table WHERE usrID = :key")
    fun get(key: Long): UserInfo?

    @Query("DELETE FROM local_user_info_table")
    fun clear()

    @Query("SELECT * FROM local_user_info_table ORDER BY lastLoginTime DESC LIMIT 1")
    fun getCurrent(): UserInfo?

    @Query("SELECT * FROM local_user_info_table WHERE currentLoginState=1")
    fun getCurrentLoginState():UserInfo?
}
