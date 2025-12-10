package com.keelim.setting.screen.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space32
import com.keelim.composeutil.resource.space8
import com.keelim.shared.data.model.ThemeType

@Composable
fun ThemeRoute(
    viewModel: ThemeViewModel = hiltViewModel(),
) {
    val themeTypeState by viewModel.themeTypeState.collectAsStateWithLifecycle()

    ThemeScreen(
        themeTypeState = themeTypeState,
        onThemeSelect = viewModel::updateThemeType
    )
}

@Composable
fun ThemeScreen(
    themeTypeState: ThemeTypeState,
    onThemeSelect: (ThemeType) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = space16),
    ) {
        Spacer(modifier = Modifier.height(space24))

        // Header Section
        ThemeHeader()

        Spacer(modifier = Modifier.height(space32))

        // Theme Selection Cards
        ThemeSelectionSection(
            selectedTheme = themeTypeState.selectedRadio,
            onThemeSelect = onThemeSelect,
        )

        Spacer(modifier = Modifier.height(space32))

        // Theme Preview
        ThemePreviewCard(
            selectedTheme = themeTypeState.selectedRadio,
        )

        Spacer(modifier = Modifier.height(space24))
    }
}

@Composable
private fun ThemeHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary,
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(space16))

            Column {
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "Customize your app theme",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ThemeSelectionSection(
    selectedTheme: ThemeType,
    onThemeSelect: (ThemeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space12),
    ) {
        Text(
            text = "Choose Theme",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(space8))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space12),
        ) {
            ThemeOptionCard(
                modifier = Modifier.weight(1f),
                title = "Light",
                subtitle = "Bright & Clean",
                isSelected = selectedTheme == ThemeType.LIGHT,
                gradientColors = listOf(
                    Color(0xFFFFF3E0),
                    Color(0xFFFFE0B2),
                ),
                iconTint = Color(0xFFF57C00),
                onClick = { onThemeSelect(ThemeType.LIGHT) },
            )

            ThemeOptionCard(
                modifier = Modifier.weight(1f),
                title = "Dark",
                subtitle = "Easy on eyes",
                isSelected = selectedTheme == ThemeType.DARK,
                gradientColors = listOf(
                    Color(0xFF263238),
                    Color(0xFF37474F),
                ),
                iconTint = Color(0xFF90CAF9),
                onClick = { onThemeSelect(ThemeType.DARK) },
            )
        }
    }
}

@Composable
private fun ThemeOptionCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    gradientColors: List<Color>,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scale",
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        },
        animationSpec = tween(durationMillis = 200),
        label = "borderColor",
    )

    Card(
        modifier = modifier
            .scale(scale),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Icon with gradient background

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(space12))

            // Selection indicator
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        ),
                )
            }
        }
    }
}

@Composable
private fun ThemePreviewCard(
    selectedTheme: ThemeType,
    modifier: Modifier = Modifier,
) {
    val previewBackgroundColor by animateColorAsState(
        targetValue = when (selectedTheme) {
            ThemeType.LIGHT -> Color(0xFFF5F5F5)
            ThemeType.DARK -> Color(0xFF1E1E1E)
        },
        animationSpec = tween(durationMillis = 300),
        label = "previewBackground",
    )

    val previewTextColor by animateColorAsState(
        targetValue = when (selectedTheme) {
            ThemeType.LIGHT -> Color(0xFF212121)
            ThemeType.DARK -> Color(0xFFE0E0E0)
        },
        animationSpec = tween(durationMillis = 300),
        label = "previewText",
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(space16),
        ) {
            Text(
                text = "Preview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(space12))

            // Mini preview
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                color = previewBackgroundColor,
                shadowElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(space12),
                ) {
                    // Simulated app bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(previewTextColor.copy(alpha = 0.8f)),
                    )

                    Spacer(modifier = Modifier.height(space12))

                    // Simulated content lines
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = 1f - (index * 0.2f))
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(previewTextColor.copy(alpha = 0.3f)),
                        )
                        Spacer(modifier = Modifier.height(space8))
                    }
                }
            }

            Spacer(modifier = Modifier.height(space12))

            Text(
                text = when (selectedTheme) {
                    ThemeType.LIGHT -> "☀️ Light mode - Perfect for daytime use"
                    ThemeType.DARK -> "🌙 Dark mode - Easier on the eyes at night"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewThemeScreen() {
    ThemeScreen(
        themeTypeState = ThemeTypeState(selectedRadio = ThemeType.LIGHT),
        onThemeSelect = { },
    )
}

@Preview
@Composable
private fun PreviewThemeScreenDark() {
    ThemeScreen(
        themeTypeState = ThemeTypeState(selectedRadio = ThemeType.DARK),
        onThemeSelect = { },
    )
}
