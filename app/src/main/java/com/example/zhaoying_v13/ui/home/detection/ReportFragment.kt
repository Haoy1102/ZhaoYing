package com.example.zhaoying_v13.ui.home.detection

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.databinding.ActivityCourseDetailBinding
import com.example.zhaoying_v13.databinding.ReportFragmentBinding
import com.example.zhaoying_v13.databinding.SelectFragmentBinding
import com.example.zhaoying_v13.network.ReportApi
import com.example.zhaoying_v13.network.ReportApiService

class ReportFragment : Fragment() {

    companion object {
        fun newInstance() = ReportFragment()
    }

    private var _binding: ReportFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReportFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)


        //TODO: 修改视频URL来源
        binding.webView.loadUrl(ReportApi.baseURL + "media/120/太极/VID_20220403_204544.mp4")


    }


}