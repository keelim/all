package com.keelim.nandadiagnosis.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SearchRecyclerViewAdapter() : RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {
    var list: List<NandaEntity> = listOf()
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true) //고유 id 를 설정
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.itemTextView.text = item.diagnosis
        holder.desView.text = item.reason
        holder.classView.text = item.class_name
        holder.domainView.text = item.domain_name

        tracker?.let {
            holder.bind(item, it.isSelected(position.toLong()))
        }
    }

    override fun getItemCount(): Int = list.size

    fun getItem(postion: Int): NandaEntity = list[postion]

    fun setNandaItem(items: List<NandaEntity>) {
        list = items
    }

    interface OnSearchItemClickListener {
        fun onSearchItemClick(position: Int)
        fun onSearchItemLongClick(position: Int)
    }

    var listener: OnSearchItemClickListener? = null

    inner class ViewHolder(binding: ItemListviewBinding, listener: OnSearchItemClickListener?) :
            RecyclerView.ViewHolder(binding.root) {
        val itemTextView: TextView = binding.diagnosisItem
        val desView: TextView = binding.diagnosisDes
        val classView = binding.className
        val domainView = binding.domainName

        init {
            binding.root.setOnClickListener {
                listener?.onSearchItemClick(adapterPosition)
            }

            binding.root.setOnLongClickListener {
                listener?.onSearchItemLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }

        fun bind(item: NandaEntity, isActivated: Boolean = false) {
            itemView.isActivated = isActivated
        }

        override fun toString(): String {
            return super.toString() + " '" + desView.text + "'"
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object : ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int = adapterPosition
                    override fun getSelectionKey(): Long? = itemId
                }
    }
}

