package com.keelim.mygrade.ui.screen.center.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import com.keelim.data.api.response.mygrade.Books
import kotlinx.coroutines.flow.flowOf

@Composable
fun BookScreen(books: LazyPagingItems<Books.Book>, modifier: Modifier = Modifier) {
    when (books.loadState.refresh) {
        LoadState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("error 가 발생하였습니다. ")
            }
        }

        is LoadState.NotLoading -> {
            LazyColumn(modifier = modifier) {
                itemsIndexed(books) { index, book ->
                    BookItem(
                        item = book,
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .background(getBackgroundForIndex(index))
                            .padding(vertical = 15.dp)
                    )
                }
            }
        }
    }
}

internal fun getBackgroundForIndex(index: Int) = if (index % 2 == 0) LightGray else Color.White

@Preview
@Composable
internal fun PreviewBookScreen() {
    BookScreen(
        books = flowOf(
            PagingData.from(
                listOf(
                    Books.Book(
                        accessInfo = null,
                        etag = null,
                        id = null,
                        kind = null,
                        saleInfo = null,
                        searchInfo = null,
                        selfLink = null,
                        volumeInfo = null
                    )
                )
            )
        ).collectAsLazyPagingItems()
    )
}

@Composable
internal fun BookItem(item: Books.Book?, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Spacer(modifier = Modifier.width(15.dp))
        val thumbnail = item?.volumeInfo?.imageLinks?.thumbnail?.replaceFirst("http", "https")
        Column(
            Modifier
                .fillMaxHeight()
                .weight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(top = 30.dp))
            AsyncImage(
                model = thumbnail,
                contentDescription = item?.volumeInfo?.title,
                modifier = Modifier
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.8f)
                .padding(30.dp)
        ) {
            Text(text = item?.volumeInfo?.title.orEmpty())

            Text(
                text =
                HtmlCompat.fromHtml(
                    item?.searchInfo?.textSnippet.orEmpty(),
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
                    .toString(),
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewBookItem() {
    BookItem(
        item =
        Books.Book(
            volumeInfo =
            Books.Book.VolumeInfo(
                allowAnonLogging = null,
                authors = listOf(),
                canonicalVolumeLink = null,
                categories = listOf(),
                contentVersion = null,
                description = null,
                imageLinks =
                Books.Book.VolumeInfo.ImageLinks(
                    smallThumbnail = null,
                    thumbnail =
                    "https://images.unsplash.com/photo-1607252650355-f7fd0460ccdb?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"
                ),
                industryIdentifiers = listOf(),
                infoLink = null,
                language = null,
                maturityRating = null,
                pageCount = null,
                panelizationSummary = null,
                previewLink = null,
                printType = null,
                publishedDate = null,
                publisher = null,
                readingModes = null,
                subtitle = null,
                title = "이것은 Title"
            ),
            searchInfo = Books.Book.SearchInfo("Description"),
            accessInfo = null,
            etag = null,
            id = null,
            kind = null,
            saleInfo = null,
            selfLink = null
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    )
}
