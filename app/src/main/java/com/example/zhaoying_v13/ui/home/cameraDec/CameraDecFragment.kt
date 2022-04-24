package com.example.zhaoying_v13.ui.home.ui.camaraDec

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.databinding.FragmentCamareDecBinding
import com.example.zhaoying_v13.databinding.SelectFragmentBinding

class CameraDecFragment : Fragment() {

    private var _binding: FragmentCamareDecBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = CameraDecFragment()
    }

    private lateinit var viewModel: CameraDecViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCamareDecBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CameraDecViewModel::class.java)
        // TODO: Use the ViewModel




    }

//    const val REQUEST_VIDEO_CAPTURE = 1
//
//    private fun dispatchTakeVideoIntent() {
//        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
//            takeVideoIntent.resolveActivity()?.also {
//                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
//            }
//        }
//    }

}