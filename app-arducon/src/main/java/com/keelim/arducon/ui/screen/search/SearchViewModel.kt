package com.keelim.arducon.ui.screen.search

import androidx.lifecycle.ViewModel
import com.keelim.data.repository.ArduconRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    arduconRepository: ArduconRepository,
) : ViewModel()
