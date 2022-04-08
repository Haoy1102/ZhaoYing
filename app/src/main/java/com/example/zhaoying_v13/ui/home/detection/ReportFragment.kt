package com.example.zhaoying_v13.ui.home.detection

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.databinding.ActivityCourseDetailBinding
import com.example.zhaoying_v13.databinding.ReportFragmentBinding
import com.example.zhaoying_v13.databinding.SelectFragmentBinding

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
        val uri=Uri.parse("android.resource://com.example.zhaoying_v13/"+R.raw.test1)
        binding.videoView.setVideoURI(uri)
        binding.startButton.setOnClickListener{
            if (!binding.videoView.isPlaying){
                binding.videoView.start()//开始播放
            }
        }
        // TODO: Use the ViewModel
    }


}