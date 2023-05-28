@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.mygrade.ui.screen.center.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.keelim.common.extensions.compose.fragmentComposeView
import com.keelim.composeutil.component.appbar.SearchView
import com.keelim.data.repository.mygrade.BookRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    repository: BookRepository,
) : ViewModel() {

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = fragmentComposeView {
        val books = viewModel.bookFlow.collectAsLazyPagingItems()
        val focusManager = LocalFocusManager.current
        MaterialTheme {
            Scaffold(topBar = {
                TopAppBar(title = {
                    SearchView(
                        query = viewModel.query.value,
                        onQueryChanged = { newQuery -> viewModel.setQuery(newQuery) },
                        onSearch = { focusManager.clearFocus() },
                        onClearQuery = { viewModel.setQuery("") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp),
                            ),
                    )
                })
            }) { padding ->
                BookScreen(books = books, modifier = Modifier.padding(padding))
            }
        }
    }
}
