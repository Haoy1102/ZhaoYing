package com.example.zhaoying_v13.ui.home.detection

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.CourseInfo
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.databinding.FragmentSelCourseBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [selCourseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelCourseFragment : Fragment() {

    private lateinit var viewModel: SelCourseViewModel

    private var _binding: FragmentSelCourseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelCourseBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = SelCourseViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SelCourseViewModel::class.java)

        initCourseMenu()

    }

    private fun initCourseMenu() {
        viewModel.courseMenuItem.observe(viewLifecycleOwner,
            Observer { it ->
                if (it != null) {
                    val items = viewModel.courseMenuItem.value

                    val layoutManager = LinearLayoutManager(getActivity());
                    //GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),3);
                    //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//                    val list = ArrayList<String>()
//                    list.add("第一章")
//                    list.add("第二章")
//                    val list1 = list.toList()

                    val cardAdaper = CardViewAdapter(requireContext(), items!!)
                    binding.recycleview.setLayoutManager(layoutManager);
                    binding.recycleview.setAdapter(cardAdaper);


                }
            })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment selCourseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelCourseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }


    private inner class CardViewAdapter internal constructor(
        private val context: Context,
        memberList: List<CourseInfo>
    ) :
        RecyclerView.Adapter<CardViewAdapter.ViewHolder>() {
        private val memberList: List<CourseInfo>
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.selcourse_card_view, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val member: CourseInfo = memberList[position]

            member.imgsrc?.let { holder.linearLayout.setBackgroundResource(it) }
            holder.itemView.setOnClickListener {
//                view.findNavController().navigate()

            }
        }

        override fun getItemCount(): Int {
            return memberList.size
        }

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var linearLayout: LinearLayout

            init {
                linearLayout =
                    itemView.findViewById<View>(R.id.selcourse_card_view_linearlayout) as LinearLayout
            }
        }

        init {
            this.memberList = memberList
        }
    }



}