package com.keelim.compose.screen.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun DeveloperScreen(navController: NavHostController) {
    var userName by remember { mutableStateOf("") }
    val onUserChanged = { text: String ->
        userName = text
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DeveloperCustomTextField(
                title = "Enter User Name",
                textState = userName,
                onTextChanged = onUserChanged
            )

            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = { }) {
                Text("register")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperCustomTextField(
    title: String,
    textState: String,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = textState,
        onValueChange = onTextChanged,
        modifier = Modifier.padding(10.dp),
        label = { Text(title) },
        singleLine = true,
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun DeveloperCustomTextFieldPreview() {
    DeveloperCustomTextField("Sample", "Sample", {})
}
