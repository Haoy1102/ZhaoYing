package com.example.zhaoying_v13.ui.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.*
import com.example.zhaoying_v13.databinding.ActivityCourseDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        //初始化UI
        initUI()
        //binding.courseNameTextview.text = courseID
//        val tv= findViewById<TextView>(R.id.textView25)
//        tv.text = courseID
        binding.elevatedButton.setOnClickListener{
            MaterialAlertDialogBuilder(this)
                .setTitle("确认")
                .setMessage("报名后立即开始课程，确认报名吗？")
                .setNegativeButton("确定") { dialog, which ->
                    updateDatabase()

                    //直接跳转至选择界面
//                    MaterialAlertDialogBuilder(this)
//                        .setTitle("提示")
//                        .setMessage("可立即体验该课程的姿态估计功能，想要现在体验吗？")
//                        .setNegativeButton("立即体验") { dialog2, which ->
//
//                        }
//                        .setPositiveButton("稍后体验",null)
//                        .show()
                }
                .setPositiveButton("取消",null)
                .show()

        }


    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        return super.onCreateView(name, context, attrs)

    }

    private fun updateDatabase(){
        lifecycleScope.launch {
            val curentUserInfo=getCurrentLoginUserInfo()
            if (curentUserInfo==null){
                Toast.makeText(this@CourseDetailActivity, "请先登录", Toast.LENGTH_LONG).show()
                return@launch
            }
            else{
                userSignUpCourse(User2Course(curentUserInfo.userID,courseID))
                Toast.makeText(this@CourseDetailActivity, "报名成功！", Toast.LENGTH_LONG).show()
                return@launch
            }

        }
    }


    private fun initUI() {
        lifecycleScope.launch {
            insertCourse()
            val courseInfo: CourseInfo? = getCourseDetailByID(courseID)
            if (courseInfo==null)
                Log.i("SELT_TAG","CourseDetailActivity找不到正确数据")
            else {
                binding.courseNameTextview.text=courseInfo.name
                binding.courseGradeTextView.text=courseInfo.grade+"  ·  "+courseInfo.timeConsumeing+"分钟"
                binding.courseAutorTextView.text=courseInfo.author
                binding.courseDetailTextView.text=courseInfo.detail
                //binding.courseGradeTextView.text
            }

        }
    }

    suspend fun insertCourse(){
        dataSource.insertCourse(CourseInfo())
    }

    suspend fun getCurrentLoginUserInfo(): UserInfo?{
        return dataSource.getCurrentLoginUserInfo()
    }
    suspend fun userSignUpCourse(user2Course: User2Course){
        return dataSource.userSignUpCourse(user2Course)
    }

    suspend fun getCourseDetailByID(courseID: String): CourseInfo? {
        return dataSource.getCourseDetailByID(courseID)
    }
}