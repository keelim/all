package com.keelim.mygrade.ui.screen.center.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalFocusManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.keelim.common.extensions.compose.fragmentComposeView
import com.keelim.data.repository.mygrade.BookRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(val repository: BookRepository) : ViewModel() {

    var query = mutableStateOf("")
        private set

    val bookFlow = repository.getBookFlow(query.value)

    fun setQuery(requestQuery: String) {
        query.value = requestQuery
    }
}

@AndroidEntryPoint
class BookFragment : Fragment() {
    private val viewModel: BookViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = fragmentComposeView {
        val books = viewModel.bookFlow.collectAsLazyPagingItems()
        val focusManager = LocalFocusManager.current
    }
}
