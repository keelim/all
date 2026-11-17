package com.keelim.comssa.ui.screen.main.finance

import com.keelim.model.finance.FinanceRssItem

/** Stable across insertion/reordering of different articles; preserve duplicate feed entries. */
internal fun financeItemKeys(items: List<FinanceRssItem>): List<String> {
    val occurrences = mutableMapOf<String, Int>()
    return items.map { item ->
        val parts = if (item.link.isNotBlank()) {
            listOf(item.source, item.link)
        } else {
            listOf(item.source, item.title, item.pubDate?.toString().orEmpty())
        }
        // Length prefixes prevent delimiter collisions in producer-supplied strings.
        val identity = parts.joinToString(separator = "") { "${it.length}:$it" }
        val occurrence = occurrences.getOrDefault(identity, 0)
        occurrences[identity] = occurrence + 1
        "finance:$identity#$occurrence"
    }
}
