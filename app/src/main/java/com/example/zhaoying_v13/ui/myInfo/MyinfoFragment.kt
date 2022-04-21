package com.example.zhaoying_v13.ui.myInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.databinding.FragmentMyinfoBinding
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyinfoFragment : Fragment() {

    private lateinit var myinfoViewModel: MyinfoViewModel
    private var _binding: FragmentMyinfoBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyinfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        developing()    //暂未开发


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val database = UserDatabase.getInstance(application)
        val dataSource = database.userDatabaseDao
        val viewModelFactory = MyinfoViewModelFactory(dataSource, application)
        myinfoViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MyinfoViewModel::class.java)

        //设置图标下的文字保存登陆状态
        setLoginTipsOrUserName()

    }

    override fun onResume() {
        super.onResume()
        myinfoViewModel.updateUserState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        binding.ivAvatar.setOnClickListener{
            requireView().findNavController().navigate(R.id.action_navigation_notifications_to_loginActivity)
        }

        binding.ivMore.setOnClickListener { v: View ->
            showMenu(v, R.menu.myinfo_more_menu)
        }
    }

    private fun developing() {
        binding.tvFavorites.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该功能暂未开放", Toast.LENGTH_SHORT).show()
        }
        binding.tvCache.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该功能暂未开放", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.editUserInfo -> {
                    Toast.makeText(requireContext(), "抱歉，该功能正在开发中", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.logOut -> {
                    myinfoViewModel.userLogout()
                    Toast.makeText(requireContext(), "退出成功", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.versionInfo -> {
                    Toast.makeText(requireContext(), "抱歉，该功能正在开发中", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }

        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }


    fun setLoginTipsOrUserName() {
        myinfoViewModel.currentUser.observe(viewLifecycleOwner,
            Observer { it ->
                if (it == null) {
                    binding.ivAvatar.setImageResource(R.drawable.ic_logo_black_76dp)
                    binding.tvLoginTipsOrUserName.setText("点击头像可以进行登录噢")
                    binding.ivAvatar.setEnabled(true)
                } else {
                    binding.tvLoginTipsOrUserName.setText(it.displayName)
                    binding.ivAvatar.setImageResource(R.mipmap.user_avatar)
                    binding.ivAvatar.setEnabled(false)
                }

            })
    }

    //Glide加载图片
//        myinfoViewModel.report.observe(viewLifecycleOwner, Observer {
//            val imgUri = it.imgSrcUrl.toUri().buildUpon().scheme("https").build()
//            Glide.with(binding.imageView.context)
//                .load(imgUri)
//                .into(binding.imageView)
//            binding.imageView.setImageURI(imgUri)
//            binding.textNotifications.text=it.imgSrcUrl
//        })


}