package com.keelim.comssa.provides

import android.content.SearchRecentSuggestionsProvider

class SuggestionProvider: SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }


    companion object {
        const val AUTHORITY = "com.keelim.comssa.provides.SuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES or DATABASE_MODE_2LINES
    }
}