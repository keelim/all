package com.keelim.cnubus.utils

import android.content.ComponentName
import android.content.Context
import androidx.window.embedding.ActivityFilter
import androidx.window.embedding.ActivityRule
import androidx.window.embedding.RuleController
import com.keelim.cnubus.ui.screen.main.MainActivity
import com.keelim.common.model.SplitManager
import javax.inject.Inject

class SplitManagerImpl @Inject constructor() : SplitManager {
    override fun createSplit(context: Context) {
        ActivityFilter(
            ComponentName(context, MainActivity::class.java),
            null
        ).let {
            ActivityRule.Builder(setOf(it))
                .setAlwaysExpand(true)
                .build()
        }.also {
            RuleController.getInstance(context).addRule(it)
        }
    }
}
