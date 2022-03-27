package com.example.zhaoying_v13.ui.myInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.database.UserDatabaseDao
import com.example.zhaoying_v13.database.UserInfo
import com.example.zhaoying_v13.databinding.FragmentMyinfoBinding
import com.example.zhaoying_v13.ui.login.ui.login.LoginViewModel

class MyinfoFragment : Fragment() {

    private lateinit var myinfoViewModel: MyinfoViewModel
    private var _binding: FragmentMyinfoBinding? = null

    private lateinit var currentUser:UserInfo

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
        val dataSource = UserDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = MyinfoViewModelFactory(dataSource, application)
        myinfoViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MyinfoViewModel::class.java)

        //设置图标下的文字
        setLoginTipsOrUserName()
    }

    fun setLoginTipsOrUserName(){
        if (myinfoViewModel.getCurrentLoginState()!=null){
            currentUser=myinfoViewModel.getCurrentLoginState()!!
            binding.tvLoginTipsOrUserName.setText(currentUser.displayName)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}