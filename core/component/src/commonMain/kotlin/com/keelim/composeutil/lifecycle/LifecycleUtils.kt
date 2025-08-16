package com.keelim.composeutil.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Lifecycle-aware utilities for Jetpack Compose
 * Helps manage resources and side effects properly in Compose
 */

/**
 * Observe lifecycle events in Compose
 * Useful for managing resources, analytics, or other lifecycle-dependent operations
 * 
 * @param onEvent Callback for lifecycle events
 */
@Composable
fun OnLifecycleEvent(
    onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit
) {
    val eventHandler by rememberUpdatedState(onEvent)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler(owner, event)
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

/**
 * Execute action only when the lifecycle is in the RESUMED state
 * Useful for actions that should only happen when the app is visible to the user
 * 
 * @param action Action to execute when resumed
 */
@Composable
fun OnLifecycleResume(action: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            action()
        }
    }
}

/**
 * Execute action when the lifecycle is in the PAUSED state
 * Useful for pausing ongoing operations when the app goes to background
 * 
 * @param action Action to execute when paused
 */
@Composable
fun OnLifecyclePause(action: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_PAUSE) {
            action()
        }
    }
}

/**
 * Execute action when the composable is first created
 * Similar to onCreate but for Compose
 * 
 * @param action Action to execute on creation
 */
@Composable
fun OnLifecycleCreate(action: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            action()
        }
    }
}

/**
 * Execute action when the composable is being destroyed
 * Useful for cleanup operations
 * 
 * @param action Action to execute on destruction
 */
@Composable
fun OnLifecycleDestroy(action: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            action()
        }
    }
}

/**
 * Execute actions when starting and stopping
 * Useful for managing ongoing operations like location updates
 * 
 * @param onStart Action to execute when starting
 * @param onStop Action to execute when stopping
 */
@Composable
fun OnLifecycleStartStop(
    onStart: () -> Unit = {},
    onStop: () -> Unit = {}
) {
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_STOP -> onStop()
            else -> { /* no-op */ }
        }
    }
}

/**
 * Execute actions for resume and pause events
 * Useful for managing UI-related operations
 * 
 * @param onResume Action to execute when resuming
 * @param onPause Action to execute when pausing
 */
@Composable
fun OnLifecycleResumePause(
    onResume: () -> Unit = {},
    onPause: () -> Unit = {}
) {
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_PAUSE -> onPause()
            else -> { /* no-op */ }
        }
    }
}

/**
 * Check if the current lifecycle is at least in the given state
 * 
 * @param state The minimum lifecycle state to check for
 * @return True if the lifecycle is at least in the given state
 */
@Composable
fun isLifecycleAtLeast(state: Lifecycle.State): Boolean {
    val lifecycleOwner = LocalLifecycleOwner.current
    return lifecycleOwner.lifecycle.currentState.isAtLeast(state)
}

/**
 * Get the current lifecycle state
 * 
 * @return Current lifecycle state
 */
@Composable
fun currentLifecycleState(): Lifecycle.State {
    val lifecycleOwner = LocalLifecycleOwner.current
    return lifecycleOwner.lifecycle.currentState
}