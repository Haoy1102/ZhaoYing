package com.example.zhaoying_v13.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.os.bundleOf
import com.example.zhaoying_v13.R

class CourseDetailActivity : AppCompatActivity() {
    companion object{
        const val COURSE_ID="COURSE_ID"
    }
    private lateinit var courseID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        courseID= intent.getStringExtra(COURSE_ID)!!
        val tv= findViewById<TextView>(R.id.textView25)
//        tv.text = courseID


    }
}