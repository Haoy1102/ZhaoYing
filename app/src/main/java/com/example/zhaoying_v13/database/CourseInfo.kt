package com.example.zhaoying_v13.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.zhaoying_v13.R
import java.net.URI

@Entity(tableName="local_course_info_table")
data class CourseInfo (
    @PrimaryKey
    val courseID:String="2",

    @ColumnInfo(name="name")
    val name: String="太极",

    @ColumnInfo(name="author")
    val author:String?="照影官方课程",

    @ColumnInfo(name="grade")
    val grade:String="初级",

    @ColumnInfo(name="timeConsumeing")
    val timeConsumeing:String="48",

    @ColumnInfo(name="digest")
    val digest:String?="太极拳是中华民族优秀的传统文化，是我国独具风格的一项强身健体、修身养性的民族传统体育项目。太极拳博大精深、流派众多、具有悠久的历史和广泛的群众基础，是中国人民在长期的实践中不断积累和丰富起来的宝贵的文化遗产。",

    @ColumnInfo(name="detail")
    val detail:String="第一课时：理论课(太极拳简介)\n" +
            "第二课时：起势\n" +
            "第三课时：左右野马分鬃\n"+
            "第四课时：白鹤亮翅\n"+
            "第五课时：左右搂膝拗步\n"+
            "第六课时：手挥琵琶\n"+
            "第七课时：左右倒卷肱\n"+
            "第八课时：单鞭\n"+
            "第九课时：云手\n",

    @ColumnInfo(name="imgsrc")
    val imgsrc:Int?= R.drawable.ic_example4
        )
