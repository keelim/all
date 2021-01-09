package com.keelim.nandadiagnosis.ui.main.search

import androidx.recyclerview.widget.DiffUtil
import com.keelim.nandadiagnosis.data.db.NandaEntity

class ListDiffCallback(val oldTodoList: List<NandaEntity>, val newTodoList: List<NandaEntity>): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldTodoList.size
    override fun getNewListSize(): Int = newTodoList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTodoList[oldItemPosition].nanda_id == newTodoList[newItemPosition].nanda_id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newTodoList[newItemPosition].equals(oldTodoList[oldItemPosition])
    }
}