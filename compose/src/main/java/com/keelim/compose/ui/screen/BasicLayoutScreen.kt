package com.keelim.compose.ui.screen

import android.graphics.pdf.PdfDocument.Page
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.compose.R


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
) {
    TextField(value = "",
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        placeholder = {
            Text(text = "Search Bar")
        }
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar()
}

@Composable
fun AlignYourBodyElement(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.sample),
            contentDescription = "sample",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(text = "Sample")
    }
}

@Preview
@Composable
fun AlignYourBodyElementPreview() {
    AlignYourBodyElement()
}

@Composable
fun FavoriteCollectionCard(
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(192.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sample),
                contentDescription = "Sample",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = "Sample",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun FavoriteCollectionCardPreview() {
    FavoriteCollectionCard()
}

@Composable
fun AlignYourBodyRow(
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(listOf(1, 2, 3)) { item ->
            AlignYourBodyElement()
        }
    }
}

@Preview
@Composable
fun AlignYourBodyRowPreview() {
    AlignYourBodyRow()
}

@Composable
fun HomeSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .paddingFromBaseline(
                    top = 40.dp,
                    bottom = 8.dp
                )
                .padding(horizontal = 16.dp)
        )
        content.invoke()
    }
}

@Preview
@Composable
fun HomeSectionPreview() {
    HomeSection(title = "안녕하세요") {
        AlignYourBodyRow()
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        SearchBar(Modifier.padding(horizontal = 16.dp))
        HomeSection(title = "안녕하세요") {
            AlignYourBodyRow()
        }

        HomeSection(title = "반갑습니다.") {
            AlignYourBodyRow()
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, heightDp = 180)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Composable
fun SoothBottomNavigation(
    modifier: Modifier = Modifier,
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        BottomNavigationItem(
            selected = true, onClick = { /*TODO*/ },icon = {
                Icon(imageVector = Icons.Default.Add, contentDescription =null )
            },
            label = {
                Text(text = "안녕")
            }
        )

        BottomNavigationItem(
            selected = true, onClick = { /*TODO*/ },icon = {
                Icon(imageVector = Icons.Default.Add, contentDescription =null )
            },
            label = {
                Text(text = "반갑")
            }
        )
    }
}

@Preview
@Composable
fun BottomNavigationPreview() {
    SoothBottomNavigation()
}

@Composable
fun Page1() {
    Scaffold(
        bottomBar = {
            SoothBottomNavigation()
        }
    ) { padding ->
        HomeScreen(Modifier.padding(padding))
    }
}

@Preview
@Composable
fun Page1Preview() {
    Page1()
}


