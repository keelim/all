package com.keelim.comssa.ui.screen.main.finance

import com.keelim.model.finance.FinanceRssItem
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FinanceItemKeysTest : FunSpec({
    val first = FinanceRssItem(source = "source", link = "https://example.com/a")
    val second = FinanceRssItem(source = "source", link = "https://example.com/b")

    test("inserting and reordering other articles preserves identity") {
        val keys = financeItemKeys(listOf(first, second))
        financeItemKeys(listOf(second, first)) shouldBe keys.reversed()
        financeItemKeys(listOf(FinanceRssItem(link = "new"), first, second)).drop(1) shouldBe keys
    }
    test("duplicate and blank feed entries retain unique keys without dropping rows") {
        val items = listOf(first, first, FinanceRssItem(), FinanceRssItem())
        val keys = financeItemKeys(items)
        keys.toSet().size shouldBe items.size
        financeItemKeys(items) shouldBe keys
    }
    test("source is part of identity and delimiter text cannot collide") {
        val items = listOf(
            first, first.copy(source = "other"),
            FinanceRssItem(source = "a:b", link = "c"),
            FinanceRssItem(source = "a", link = "b:c"),
        )
        financeItemKeys(items).toSet().size shouldBe items.size
    }
    test("article content updates preserve link identity") {
        financeItemKeys(listOf(first.copy(title = "changed", description = "updated"))) shouldBe
            financeItemKeys(listOf(first))
    }
})
