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
import com.example.zhaoying_v13.databinding.FragmentMyinfoBinding

class MyinfoFragment : Fragment() {

    private lateinit var myinfoViewModel: MyinfoViewModel
    private var _binding: FragmentMyinfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myinfoViewModel =
            ViewModelProvider(this).get(MyinfoViewModel::class.java)

        _binding = FragmentMyinfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        myinfoViewModel.report.observe(viewLifecycleOwner, Observer {
            val imgUri = it.imgSrcUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(binding.imageView.context)
                .load(imgUri)
                .into(binding.imageView)
            binding.imageView.setImageURI(imgUri)
            binding.textNotifications.text=it.imgSrcUrl
        })

        binding.ivAvatar.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_notifications_to_loginActivity)
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}