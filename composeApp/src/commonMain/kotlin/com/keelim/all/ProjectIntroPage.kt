package com.keelim.all

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProjectIntroPage() {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ALL 프로젝트",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "여러 플랫폼에서 동작하는 멀티플랫폼 Compose 프로젝트입니다.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "주요 기능",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Text("- 데스크탑, 웹, 안드로이드, iOS 지원")
                    Text("- 공통 UI 컴포넌트 제공")
                    Text("- 모듈화된 구조")
                    Text("- 최신 Compose 활용")
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    uriHandler.openUri("https://github.com/keelim/all")
                }) {
                    Text("깃허브에서 보기")
                }
            }
        }
    }
} 