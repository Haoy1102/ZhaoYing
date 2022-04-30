package com.example.zhaoying_v13.ui.home.detection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.databinding.ReportFragmentBinding
import com.example.zhaoying_v13.databinding.SectionCheckFragmentBinding

class sectionCheckFragment : Fragment() {

    private var _binding: SectionCheckFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = sectionCheckFragment()
    }

    private lateinit var viewModel: SectionCheckViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.section_check_fragment, container, false)
        _binding = SectionCheckFragmentBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(getActivity());
        //GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),3);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        val list= ArrayList<String>()
        list.add("第一章")
        list.add("第二章")
        val list1=list.toList()
        val cardAdaper=CardViewAdapter(requireContext(), list1)
        binding.recycleview.setLayoutManager(layoutManager);

        binding.recycleview.setAdapter(cardAdaper);




        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SectionCheckViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

private class CardViewAdapter internal constructor(
    private val context: Context,
    memberList: List<String>
) :
    RecyclerView.Adapter<CardViewAdapter.ViewHolder>() {
    private val memberList: List<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.section_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member: String = memberList[position]

        holder.cardTile.setText(java.lang.String.valueOf(member))

        holder.itemView.setOnClickListener {
            Toast.makeText(context,holder.cardTile.text.toString(),Toast.LENGTH_SHORT).show()

        }
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cardTile: TextView

        init {
            cardTile = itemView.findViewById<View>(R.id.cardTile) as TextView
        }
    }

    init {
        this.memberList = memberList
    }
}
