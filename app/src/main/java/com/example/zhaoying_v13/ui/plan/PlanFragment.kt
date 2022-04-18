package com.example.zhaoying_v13.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.zhaoying_v13.databinding.FragmentPlanBinding

class PlanFragment : Fragment() {

    private lateinit var planViewModel: PlanViewModel
    private var _binding: FragmentPlanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        planViewModel = ViewModelProvider(this).get(PlanViewModel::class.java)
        _binding = FragmentPlanBinding.inflate(inflater, container, false)


        developing()
        return binding.root
    }

    private fun developing(){
        binding.floatingActionButton.setOnClickListener{
            Toast.makeText(requireContext(),"抱歉，该功能暂未开放", Toast.LENGTH_SHORT).show()
        }
        binding.floatingActionButtonIncard.setOnClickListener {
            Toast.makeText(requireContext(),"抱歉，该功能暂未开放",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}