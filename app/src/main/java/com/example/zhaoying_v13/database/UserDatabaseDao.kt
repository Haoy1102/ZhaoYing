package com.example.zhaoying_v13.database;

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.Deferred

@Dao
public interface UserDatabaseDao {
    @Insert
    suspend fun insert(user:UserInfo)

    @Update
    fun update(user:UserInfo)

    @Query("SELECT * from local_user_info_table WHERE userID = :key ORDER BY lastLoginTime DESC LIMIT 1")
    suspend fun getUserByID(key: String): UserInfo?

    @Query("DELETE FROM local_user_info_table")
    fun clear()

    @Query("SELECT * FROM local_user_info_table ORDER BY lastLoginTime DESC LIMIT 1")
    fun getCurrent(): LiveData<UserInfo>

    @Query("SELECT * FROM local_user_info_table")
    suspend fun getAllUser(): List<UserInfo?>

    //得到登陆状态为1的一个人
    @Query("SELECT * FROM local_user_info_table WHERE currentLoginState=1 ORDER BY lastLoginTime DESC LIMIT 1")
    suspend fun getCurrentLoginUserInfo():UserInfo?

    //得到登陆状态为1的个数
    @Query("select count() from local_user_info_table where currentLoginState=1")
    fun getCurrentLoginUserNum(): Int

    @Query("update local_user_info_table set currentLoginState=0 where userID!=:key")
    suspend fun setOtherLogin0(key: String)

    @Query("update local_user_info_table set currentLoginState=1 where userID=:key")
    suspend fun setCurrentLogin1(key: String)

    @Insert
    suspend fun insertCourse(courseInfo:CourseInfo)

    @Transaction
    @Query("select * from local_user_info_table where userID = :key")
    fun getUsersWithCourses(key: String):List<UserWithCourses>

    @Query("select * from local_course_info_table where courseID=:key")
    suspend fun getCourseDetailByID(key:String):CourseInfo?

}
