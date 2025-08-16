package com.keelim.composeutil.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp

/**
 * Commonly used Modifier extensions following Android Compose best practices
 * Helps maintain consistency across the application
 */

/**
 * Modifier extension for clickable without ripple effect
 * Useful for custom clickable components where ripple is not desired
 * 
 * @param enabled Whether the component is enabled
 * @param onClickLabel Semantic/accessibility label for the click action
 * @param role Semantic role for accessibility
 * @param onClick Click handler
 * @return Modifier with clickable behavior but no ripple
 */
fun Modifier.clickableNoRipple(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickableNoRipple"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick
    )
}

/**
 * Modifier extension for standard horizontal padding
 * Uses Material Design 3 spacing guidelines
 * 
 * @return Modifier with horizontal padding based on Material 3 guidelines
 */
@Composable
fun Modifier.horizontalPadding(): Modifier = padding(horizontal = MaterialTheme.spacing.medium)

/**
 * Modifier extension for standard vertical padding
 * Uses Material Design 3 spacing guidelines
 * 
 * @return Modifier with vertical padding based on Material 3 guidelines
 */
@Composable
fun Modifier.verticalPadding(): Modifier = padding(vertical = MaterialTheme.spacing.medium)

/**
 * Modifier extension for standard all-around padding
 * Uses Material Design 3 spacing guidelines
 * 
 * @return Modifier with padding on all sides based on Material 3 guidelines
 */
@Composable
fun Modifier.standardPadding(): Modifier = padding(MaterialTheme.spacing.medium)

/**
 * Modifier extension for small padding
 * Uses Material Design 3 spacing guidelines
 * 
 * @return Modifier with small padding on all sides
 */
@Composable
fun Modifier.smallPadding(): Modifier = padding(MaterialTheme.spacing.small)

/**
 * Modifier extension for large padding
 * Uses Material Design 3 spacing guidelines
 * 
 * @return Modifier with large padding on all sides
 */
@Composable
fun Modifier.largePadding(): Modifier = padding(MaterialTheme.spacing.large)

/**
 * Extension for Material Theme spacing values
 * Provides consistent spacing throughout the app
 */
val androidx.compose.material3.MaterialTheme.spacing: AppSpacing
    @Composable get() = AppSpacing

object AppSpacing {
    val extraSmall: Dp @Composable get() = androidx.compose.ui.unit.dp * 4
    val small: Dp @Composable get() = androidx.compose.ui.unit.dp * 8
    val medium: Dp @Composable get() = androidx.compose.ui.unit.dp * 16
    val large: Dp @Composable get() = androidx.compose.ui.unit.dp * 24
    val extraLarge: Dp @Composable get() = androidx.compose.ui.unit.dp * 32
}

/**
 * Conditional modifier application
 * Applies a modifier only if the condition is true
 * 
 * @param condition The condition to check
 * @param modifier The modifier to apply if condition is true
 * @return Original modifier or modified version based on condition
 */
inline fun Modifier.conditional(
    condition: Boolean,
    crossinline modifier: Modifier.() -> Modifier
): Modifier = if (condition) {
    then(modifier())
} else {
    this
}

/**
 * Conditional modifier application with else clause
 * Applies different modifiers based on condition
 * 
 * @param condition The condition to check
 * @param ifTrue Modifier to apply if condition is true
 * @param ifFalse Modifier to apply if condition is false
 * @return Modified version based on condition
 */
inline fun Modifier.conditionalThen(
    condition: Boolean,
    crossinline ifTrue: Modifier.() -> Modifier,
    crossinline ifFalse: Modifier.() -> Modifier = { this }
): Modifier = if (condition) {
    then(ifTrue())
} else {
    then(ifFalse())
}

/**
 * Standard fill max width with padding
 * Common pattern used throughout the app
 * 
 * @return Modifier with fillMaxWidth and horizontal padding
 */
@Composable
fun Modifier.fillMaxWidthWithPadding(): Modifier = 
    fillMaxWidth().horizontalPadding()