package com.keelim.nandadiagnosis.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SearchRecyclerViewAdapter(private var values: List<NandaEntity>) :
    RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {

    init {
        setHasStableIds(true) //고유 id 를 설정
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemTextView.text = item.diagnosis
        holder.desView.text = item.reason
        holder.classView.text = item.class_name
        holder.domainView.text = item.domain_name
    }

    override fun getItemCount(): Int = values.size

    fun getItem(postion: Int): NandaEntity = values[postion]

    fun setNandaItem(items: List<NandaEntity>) {
        val disposable = Observable.just(items)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .map { DiffUtil.calculateDiff(ListDiffCallback(this.values, items)) }
            .subscribe({
                this.values = items
                it.dispatchUpdatesTo(this)
            }, {

            })
        disposable.dispose()
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

        fun bind(isActivated: Boolean = false) {
            itemView.isActivated = isActivated
        }

        override fun toString(): String {
            return super.toString() + " '" + desView.text + "'"
        }
    }
}

