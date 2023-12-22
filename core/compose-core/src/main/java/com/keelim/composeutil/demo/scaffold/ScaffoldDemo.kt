package com.keelim.composeutil.demo.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
fun ScaffoldDemo() {
    val counter = remember {
        mutableIntStateOf(0)
    }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(title = {
                Text(text = "Simple counter app")
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (counter.value < 10) {
                        counter.value++
                    } else {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                "Counter Finished",
                            )
                        }
                    }
                },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (counter.value == 10) {
                    "Counter finished"
                } else {
                    "Counter:${counter.value}"
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScaffoldDemo() {
    ScaffoldDemo()
}
