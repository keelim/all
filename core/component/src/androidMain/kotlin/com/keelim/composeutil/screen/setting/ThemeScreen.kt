package com.keelim.composeutil.screen.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ThemeScreen(navController: NavHostController) {
    var userName by remember { mutableStateOf("") }
    val onUserChanged = { text: String ->
        userName = text
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DeveloperCustomTextField(
                title = "Enter User Name",
                textState = userName,
                onTextChanged = onUserChanged,
            )

            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = { }) {
                Text("register")
            }
        }
    }
}
