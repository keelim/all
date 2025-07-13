package com.keelim.all

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ProjectIntroPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFe3f2fd), Color(0xFFbbdefb))
                )
            )
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            ProjectTitleAndDescription()
            MainFeaturesCard()
            TechStackCard()
            TeamMembersCard()
        }
        FooterButtons()
    }
}

@Composable
fun ProjectTitleAndDescription() {
    Text(
        text = "ALL Project",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF0d47a1)
    )
    Text(
        text = "All-in-one project based on Kotlin Multiplatform + Compose",
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFF1976d2),
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun MainFeaturesCard() {
    Card(
        modifier = Modifier
            .padding(top = 8.dp)
            .heightIn(min = 220.dp)
            .shadow(8.dp, RoundedCornerShape(32.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(40.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Main Features",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF1565c0)
            )
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💻", fontSize = 20.sp)
                Spacer(Modifier.size(8.dp))
                Text("Supports Desktop, Web, Android, iOS", fontSize = 18.sp, lineHeight = 28.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🧩", fontSize = 20.sp)
                Spacer(Modifier.size(8.dp))
                Text("Provides common UI components", fontSize = 18.sp, lineHeight = 28.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🗂️", fontSize = 20.sp)
                Spacer(Modifier.size(8.dp))
                Text("Modular architecture", fontSize = 18.sp, lineHeight = 28.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 20.sp)
                Spacer(Modifier.size(8.dp))
                Text("Utilizes latest Compose", fontSize = 18.sp, lineHeight = 28.sp)
            }
        }
    }
}

@Composable
fun TechStackCard() {
    Card(
        modifier = Modifier.padding(top = 8.dp).shadow(4.dp, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Tech Stack", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Spacer(Modifier.height(12.dp))
            FlowRow(
                maxItemsInEachRow = 5
            ) {
                TechChip("Kotlin", Color(0xFF7F52FF))
                TechChip("Compose", Color(0xFF4285F4))
                TechChip("Material3", Color(0xFF00BFAE))
                TechChip("Multiplatform", Color(0xFFFFB300))
                TechChip("Gradle", Color(0xFF02303A))
                TechChip("Ktor", Color(0xFF009688))
                TechChip("Room", Color(0xFF795548))
                TechChip("DataStore", Color(0xFF607D8B))
                TechChip("Firebase", Color(0xFFFFC107))
                TechChip("OkHttp", Color(0xFF4CAF50))
            }
        }
    }
}

@Composable
fun TeamMembersCard() {
    Card(
        modifier = Modifier.padding(top = 8.dp).shadow(4.dp, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Team", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                MemberProfile("K", "keelim", Color(0xFF90caf9))
                MemberProfile("J", "jaehyun", Color(0xFFb39ddb))
            }
        }
    }
}

@Composable
fun BoxScope.FooterButtons(
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    Surface(
        modifier = modifier
            .align(Alignment.BottomEnd)
            .padding(end = 24.dp, bottom = 24.dp)
            .shadow(8.dp, RoundedCornerShape(32.dp)),
        color = Color.White.copy(alpha = 0.7f),
        shape = RoundedCornerShape(32.dp),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { uriHandler.openUri("https://github.com/keelim/all") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF24292f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Spacer(Modifier.size(8.dp))
                Text("GitHub")
            }
            Button(
                onClick = { uriHandler.openUri("mailto:contact@keelim.com") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976d2),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    Icons.Filled.AlternateEmail,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.size(8.dp))
                Text("Contact")
            }
        }
    }
}

@Composable
fun TechChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun MemberProfile(initial: String, name: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(color)
                .shadow(8.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(initial, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        }
        Spacer(Modifier.height(4.dp))
        Text(name, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF333333))
    }
}
