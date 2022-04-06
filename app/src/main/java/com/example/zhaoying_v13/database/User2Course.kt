package com.example.zhaoying_v13.database

import androidx.room.*

@Entity(tableName="local_user2course_table", primaryKeys = ["userID","courseID"])
data class User2Course(
    val userID:String,
    val courseID:String
)

data class UserWithCourses(
    @Embedded val user: UserInfo,
    @Relation(
        parentColumn = "userID",
        entityColumn = "courseID",
        associateBy = Junction(User2Course::class)
    )
    val courses: List<CourseInfo>

)

