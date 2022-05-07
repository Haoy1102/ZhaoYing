package com.example.zhaoying_v13.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphNavigator
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.databinding.FragmentHomeBinding
import com.example.zhaoying_v13.ui.home.cameraX.CameraXActivity

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        init()
        developing()
        return binding.root
    }

    private fun init() {
        binding.igbtnDetection.setOnClickListener{
            requireView().findNavController().navigate(R.id.action_navigation_home_to_detectionActivity)
        }
        binding.igbtnTakeAction.setOnClickListener {
//            requireView().findNavController().navigate(R.id.action_navigation_home_to_cameraDecActivity)

            startActivity(Intent(context, CameraXActivity::class.java))
        }
        //太极图片点击
        binding.imageView7.setOnClickListener {
            startActivity(
                Intent(context, CourseDetailActivity::class.java)
                    .putExtra(CourseDetailActivity.COURSE_ID, "2")
            )
        }
    }

    private fun developing() {
        binding.imageButton2.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该功能暂未开放", Toast.LENGTH_SHORT).show()
        }
        binding.imageButton4.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该功能暂未开放", Toast.LENGTH_SHORT).show()
        }
        binding.imageButton5.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该功能暂未开放", Toast.LENGTH_SHORT).show()
        }
        binding.imageButton6.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该功能暂未开放", Toast.LENGTH_SHORT).show()
        }
        binding.imageButton7.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该功能暂未开放", Toast.LENGTH_SHORT).show()
        }
        binding.imageButton8.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该功能暂未开放", Toast.LENGTH_SHORT).show()
        }
        binding.imageView4.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该课程暂未上架", Toast.LENGTH_SHORT).show()
        }
        binding.imageView5.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该课程暂未上架", Toast.LENGTH_SHORT).show()
        }
        binding.imageView6.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该课程暂未上架", Toast.LENGTH_SHORT).show()
        }
        binding.imageView8.setOnClickListener {
            Toast.makeText(requireContext(), "抱歉，该课程暂未上架", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}