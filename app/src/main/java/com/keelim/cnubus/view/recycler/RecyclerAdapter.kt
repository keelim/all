package com.keelim.cnubus.view.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.R
import java.util.*

class RecyclerAdapter // 생성자에서 데이터 리스트 객체를 전달받음.
internal constructor(private val mData: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textView1: TextView

        init {
            itemView.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    mData[pos] = "item clicked. pos=$pos"
                    notifyItemChanged(pos)
                }
            }
            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.text1)
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.items_recycler, parent, false)
        return ViewHolder(view)
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = mData[position]
        holder.textView1.text = text
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    override fun getItemCount(): Int = mData.size


}