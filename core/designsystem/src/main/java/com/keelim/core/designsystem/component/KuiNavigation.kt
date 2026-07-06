package com.keelim.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keelim.core.designsystem.theme.KuiTheme

enum class KuiOnboardingStepState { Pending, Current, Completed, Skipped }

data class KuiOnboardingStep(
    val id: String,
    val title: String,
    val description: String? = null,
    val state: KuiOnboardingStepState = KuiOnboardingStepState.Pending,
    val enabled: Boolean = true,
)

@Composable
fun KuiScrollArea(
    modifier: Modifier = Modifier,
    horizontal: Boolean = false,
    content: @Composable () -> Unit,
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = if (horizontal) {
            modifier.horizontalScroll(scrollState)
        } else {
            modifier.verticalScroll(scrollState)
        },
    ) {
        content()
    }
}

@Composable
fun KuiTabsList(
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    PrimaryTabRow(
        selectedTabIndex = selectedIndex.coerceAtLeast(0),
        modifier = modifier,
        containerColor = KuiTheme.colorScheme.surface,
        contentColor = KuiTheme.colorScheme.primary,
        tabs = content,
    )
}

@Composable
fun KuiTabsTrigger(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        text = {
            Text(
                text = text,
                style = KuiTheme.typography.labelLarge,
                color = if (selected) {
                    KuiTheme.colorScheme.primary
                } else {
                    KuiTheme.colorScheme.onSurfaceVariant
                },
            )
        },
    )
}

@Composable
fun KuiTabsContent(
    selected: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(visible = selected, modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
fun KuiOnboardingTour(
    steps: List<KuiOnboardingStep>,
    currentStepId: String?,
    onStepSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(KuiOnboardingStep?) -> Unit = {},
) {
    val currentStep = steps.firstOrNull { it.id == currentStepId }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space4),
    ) {
        KuiOnboardingStepList(
            steps = steps,
            currentStepId = currentStepId,
            onStepSelected = onStepSelected,
        )
        KuiOnboardingHighlight(
            active = currentStep != null,
            completed = currentStep?.state == KuiOnboardingStepState.Completed,
            skipped = currentStep?.state == KuiOnboardingStepState.Skipped,
            contentDescription = currentStep?.title,
        ) {
            content(currentStep)
        }
    }
}

@Composable
fun KuiOnboardingStepList(
    steps: List<KuiOnboardingStep>,
    currentStepId: String?,
    onStepSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
    ) {
        steps.forEachIndexed { index, step ->
            val selected = step.id == currentStepId || step.state == KuiOnboardingStepState.Current
            KuiOnboardingStepRow(
                index = index + 1,
                step = step,
                selected = selected,
                onClick = { onStepSelected(step.id) },
            )
        }
    }
}

@Composable
fun KuiOnboardingHighlight(
    active: Boolean,
    completed: Boolean,
    skipped: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val borderColor = when {
        completed -> KuiTheme.colors.success
        skipped -> KuiTheme.colors.warning
        active -> KuiTheme.colorScheme.primary
        else -> KuiTheme.colorScheme.outline
    }
    val backgroundColor = when {
        completed -> KuiTheme.colors.successContainer
        skipped -> KuiTheme.colors.warningContainer
        active -> KuiTheme.colors.surfaceSoft
        else -> KuiTheme.colorScheme.surface
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription?.let { this.contentDescription = it }
            },
        shape = KuiTheme.shapes.large,
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Column(
            modifier = Modifier.padding(KuiTheme.spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
            content = content,
        )
    }
}

@Composable
private fun KuiOnboardingStepRow(
    index: Int,
    step: KuiOnboardingStep,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val container = when (step.state) {
        KuiOnboardingStepState.Completed -> KuiTheme.colors.successContainer
        KuiOnboardingStepState.Skipped -> KuiTheme.colors.warningContainer
        KuiOnboardingStepState.Current -> KuiTheme.colors.surfaceSoft
        KuiOnboardingStepState.Pending -> KuiTheme.colorScheme.surface
    }
    val border = if (selected) KuiTheme.colorScheme.primary else KuiTheme.colorScheme.outline
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(container, KuiTheme.shapes.medium)
            .clickable(
                enabled = step.enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .semantics { contentDescription = step.title }
            .padding(KuiTheme.spacing.space3),
        horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space3),
        verticalAlignment = Alignment.Top,
    ) {
        Surface(
            shape = KuiTheme.shapes.small,
            color = if (selected) KuiTheme.colorScheme.primary else KuiTheme.colors.surfaceStrong,
            border = BorderStroke(1.dp, border),
        ) {
            Text(
                text = index.toString(),
                modifier = Modifier.padding(
                    horizontal = KuiTheme.spacing.space2,
                    vertical = KuiTheme.spacing.space1,
                ),
                style = KuiTheme.typography.labelMedium,
                color = if (selected) {
                    KuiTheme.colorScheme.onPrimary
                } else {
                    KuiTheme.colorScheme.onSurface
                },
                fontWeight = FontWeight.Medium,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space1),
        ) {
            Text(
                text = step.title,
                style = KuiTheme.typography.bodyMedium,
                color = KuiTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )
            step.description?.let {
                Text(
                    text = it,
                    style = KuiTheme.typography.bodySmall,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
internal fun KuiNavigationSample() {
    KuiScrollArea {
        KuiTabsList(selectedIndex = 0) {
            KuiTabsTrigger(selected = true, onClick = {}, text = "Tab")
        }
        KuiTabsContent(selected = true) {
            Text(
                text = "Content",
                style = KuiTheme.typography.bodyMedium,
                color = KuiTheme.colorScheme.onSurface,
            )
        }
    }
    KuiOnboardingTour(
        steps = listOf(KuiOnboardingStep(id = "one", title = "Title")),
        currentStepId = "one",
        onStepSelected = {},
    )
}
