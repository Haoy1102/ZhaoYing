package com.example.zhaoying_v13.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.CourseInfo
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.databinding.ActivityCourseDetailBinding
import kotlinx.coroutines.launch

class CourseDetailActivity : AppCompatActivity() {
    companion object {
        const val COURSE_ID = "COURSE_ID"
    }

    private lateinit var binding: ActivityCourseDetailBinding
    private lateinit var dataSource: UserDatabaseDao
    private lateinit var courseID: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dataSource = UserDatabase.getInstance(application).userDatabaseDao
        courseID = intent.getStringExtra(COURSE_ID)!!
        //binding.courseNameTextview.text = courseID
//        val tv= findViewById<TextView>(R.id.textView25)
//        tv.text = courseID


    }

//    private fun initUI() {
//        lifecycleScope.launch {
//            val courseInfo: CourseInfo? = getCourseDetailByID(courseID)
//            if (courseInfo==null)
//                Log.i("SELT_TAG","CourseDetailActivity找不到正确数据")
//            else{
////                binding.course_name_textview
//            }
//
//        }
//    }

//    suspend fun getCourseDetailByID(courseID: String): CourseInfo? {
//        return dataSource.getCourseDetailByID(courseID)
//    }
}