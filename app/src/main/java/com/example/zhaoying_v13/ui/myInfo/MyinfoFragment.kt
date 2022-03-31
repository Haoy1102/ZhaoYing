package com.example.zhaoying_v13.ui.myInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.databinding.FragmentMyinfoBinding

class MyinfoFragment : Fragment() {

    private lateinit var myinfoViewModel: MyinfoViewModel
    private var _binding: FragmentMyinfoBinding? = null

    //private lateinit var currentUser:UserInfo

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentMyinfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Glide加载图片
//        myinfoViewModel.report.observe(viewLifecycleOwner, Observer {
//            val imgUri = it.imgSrcUrl.toUri().buildUpon().scheme("https").build()
//            Glide.with(binding.imageView.context)
//                .load(imgUri)
//                .into(binding.imageView)
//            binding.imageView.setImageURI(imgUri)
//            binding.textNotifications.text=it.imgSrcUrl
//        })

        //设置


        binding.ivAvatar.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_notifications_to_loginActivity)
        )

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val database=UserDatabase.getInstance(application)
        val dataSource = database.userDatabaseDao
        val viewModelFactory = MyinfoViewModelFactory(dataSource, application)
        myinfoViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MyinfoViewModel::class.java)

        //设置图标下的文字保存登陆状态
        setLoginTipsOrUserName()
    }


    fun setLoginTipsOrUserName(){
        Log.i("Database","1:setLoginTipsOrUserName()执行")
        myinfoViewModel.currentUser.observe(viewLifecycleOwner,
            Observer { it->
                if (it==null)
                    binding.tvLoginTipsOrUserName.setText("点击头像可以进行登录噢！")
                else{
                    binding.tvLoginTipsOrUserName.setText(it.displayName)
                }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}