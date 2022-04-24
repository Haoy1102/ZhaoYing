package com.example.zhaoying_v13.ui.home.detection

import android.app.ProgressDialog
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
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
    private var enterFrom=1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReportFragmentBinding.inflate(inflater, container, false)
        if (arguments?.getInt("enterFrom")!=null){
            enterFrom= requireArguments().getInt("enterFrom")
        }
        if (enterFrom==0) {
            binding.tvReportDetail.setText(arguments?.getString("evaluate"))
            val videoURL=arguments?.getString("url")!!
            binding.webView.loadUrl(ReportApi.baseURL+"media/"+videoURL)


            Log.i("TAGURL",ReportApi.baseURL+"media/"+videoURL)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        binding.btnVideoTrans.setOnClickListener {
            if(binding.btnVideoTrans.text.toString()=="查看标准视频"){
                binding.btnVideoTrans.setText("查看分析视频")
                binding.webView.loadUrl(ReportApi.baseURL+"media/"+requireArguments().getString("criterion"))
                return@setOnClickListener
            }
            if (binding.btnVideoTrans.text.toString()=="查看分析视频"){
                binding.btnVideoTrans.setText("查看标准视频")
                val videoURL=arguments?.getString("url")!!
                binding.webView.loadUrl(ReportApi.baseURL+"media/"+videoURL)
                return@setOnClickListener
            }

        }
    }


//    private fun loadUI(){
//        if (viewModel.getEnterFrom()==1){
//            binding.webView.loadUrl(ReportApi.baseURL + viewModel.report.value!!.url)
//            binding.tvReportDetail.setText(viewModel.report.value!!.evaluate)
//        }
//
//    }


}