package com.keelim.composeutil.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T : Any> KeelimNavDisplay(
    backStack: SnapshotStateList<T>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { backStack.removeLastOrNull() },
    entries: EntryProviderScope<T>.() -> Unit,
) {
    val motionScheme = KuiTheme.motionScheme

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                fadeOut(motionScheme.defaultEffectsSpec()),
            )
        },
        popTransitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                scaleOut(
                    targetScale = 0.7f,
                ),
            )
        },
        entryProvider = entryProvider(builder = entries),
    )
}
