package com.keelim.nandadiagnosis.data

import java.util.*

object SearchRecyclerItem {
    val ITEMS: MutableList<PlaceholderItem> = ArrayList()

    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createPlaceholderItem(i))
        }
    }

    private fun addItem(item: PlaceholderItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    private fun createPlaceholderItem(position: Int): PlaceholderItem {
        return PlaceholderItem(position.toString(), "Item $position", makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class PlaceholderItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}