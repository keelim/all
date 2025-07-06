package com.keelim.arducon.ui.screen.stats

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.keelim.model.DeepLink
import com.keelim.model.UsageStat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val topUsedLinks by viewModel.topUsedLinks.collectAsState()
    val recentUsedLinks by viewModel.recentUsedLinks.collectAsState()
    val dailyUsageStats by viewModel.dailyUsageStats.collectAsState()

    StatsScreen(topUsedLinks, recentUsedLinks, dailyUsageStats)
}

@Composable
private fun StatsScreen(
    topUsedLinks: List<DeepLink>,
    recentUsedLinks: List<DeepLink>,
    dailyUsageStats: List<UsageStat>
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        StatsSectionCard(
            title = "가장 많이 사용한 링크",
            icon = Icons.Default.Star,
            content = {
                if (topUsedLinks.isEmpty()) {
                    Text("데이터 없음", style = MaterialTheme.typography.bodyMedium)
                } else {
                    topUsedLinks.forEachIndexed { idx, item ->
                        StatsLinkItem(item, highlight = idx == 0)
                        if (idx != topUsedLinks.lastIndex) Divider(
                            modifier = Modifier.padding(
                                vertical = 4.dp
                            )
                        )
                    }
                }
            }
        )
        StatsSectionCard(
            title = "최근 사용한 링크",
            icon = Icons.Default.CheckCircle,
            content = {
                if (recentUsedLinks.isEmpty()) {
                    Text("데이터 없음", style = MaterialTheme.typography.bodyMedium)
                } else {
                    recentUsedLinks.forEachIndexed { idx, item ->
                        StatsLinkItem(item, highlight = false)
                        if (idx != recentUsedLinks.lastIndex) Divider(
                            modifier = Modifier.padding(
                                vertical = 4.dp
                            )
                        )
                    }
                }
            }
        )
        StatsSectionCard(
            title = "일별 사용량",
            icon = Icons.Default.DateRange,
            content = {
                StatsBarChart(dailyUsageStats)
            }
        )
    }
}

@Preview
@Composable
private fun StatsScreenPreview() {
    val topUsedLinks = listOf(
        DeepLink(url = "https://example.com/link1", title = "Example Link 1", usageCount = 10, timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24),
        DeepLink(url = "https://example.com/link2", title = "Example Link 2", usageCount = 5, timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 48),
    )
    val recentUsedLinks = listOf(
        DeepLink(url = "https://example.com/link3", title = "Example Link 3", usageCount = 2, timestamp = System.currentTimeMillis() - 1000 * 60 * 30),
        DeepLink(url = "https://example.com/link4", title = "Example Link 4", usageCount = 1, timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2),
    )
    val dailyUsageStats = listOf(
        UsageStat(day = "2023-01-01", count = 5),
        UsageStat(day = "2023-01-02", count = 10),
        UsageStat(day = "2023-01-03", count = 7),
    )
    StatsScreen(
        topUsedLinks = topUsedLinks,
        recentUsedLinks = recentUsedLinks,
        dailyUsageStats = dailyUsageStats
    )
}
@Composable
private fun StatsSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 8.dp)
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Divider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            content()
        }
    }
}

@Composable
private fun StatsLinkItem(item: DeepLink, highlight: Boolean) {
    val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
    val lastUsed = if (item.lastUsed > 0) dateFormat.format(Date(item.lastUsed)) else "-"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (highlight) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (highlight) Color.White else MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title.ifEmpty { item.url },
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal),
                color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.url,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${item.usageCount}회",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = lastUsed,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatsBarChart(stats: List<UsageStat>) {
    if (stats.isEmpty()) {
        Text("데이터 없음", style = MaterialTheme.typography.bodyMedium)
        return
    }
    BarChart(
        barChartData = BarChartData(
            bars = stats.map {
                BarChartData.Bar(
                    label = it.day,
                    value = it.count.toFloat(),
                    color = Color(
                        android.graphics.Color.HSVToColor(
                            floatArrayOf((it.day.hashCode() % 360).toFloat(), 0.5f, 0.85f)
                        )
                    )
                )
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        animation = tween(),
        labelDrawer = SimpleValueDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(),
    )
}

