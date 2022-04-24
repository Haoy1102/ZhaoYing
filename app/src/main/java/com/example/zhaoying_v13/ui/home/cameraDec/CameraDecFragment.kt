package com.example.zhaoying_v13.ui.home.ui.camaraDec

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zhaoying_v13.R

class CameraDecFragment : Fragment() {

    companion object {
        fun newInstance() = CameraDecFragment()
    }

    private lateinit var viewModel: CameraDecViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_camare_dec, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CameraDecViewModel::class.java)
        // TODO: Use the ViewModel
    }

}