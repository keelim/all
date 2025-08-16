package com.keelim.composeutil.performance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Performance optimization utilities for Jetpack Compose
 * Helps with recomposition avoidance and efficient state management
 */

/**
 * Creates a derived state that only recomposes when the calculation result changes
 * Helps avoid unnecessary recompositions in complex UI hierarchies
 * 
 * @param calculation The expensive calculation to derive state from
 * @return A derived state that only updates when the result actually changes
 */
@Composable
inline fun <T> rememberDerivedStateOf(
    crossinline calculation: () -> T
) = remember { derivedStateOf { calculation() } }

/**
 * Convert a regular List to ImmutableList for better Compose performance
 * ImmutableList helps Compose optimize recompositions
 * 
 * @param list The source list to convert
 * @return ImmutableList version of the input list
 */
fun <T> List<T>.toImmutableListSafe(): ImmutableList<T> = when {
    this is ImmutableList<T> -> this
    isEmpty() -> persistentListOf()
    else -> toImmutableList()
}

/**
 * Stable wrapper for list data that implements proper equality
 * Helps Compose skip recompositions when list content hasn't actually changed
 * 
 * @param data The list data to wrap
 */
@androidx.compose.runtime.Stable
data class StableListWrapper<T>(
    val data: ImmutableList<T>
) {
    constructor(data: List<T>) : this(data.toImmutableListSafe())
    
    val size: Int get() = data.size
    val isEmpty: Boolean get() = data.isEmpty()
    
    operator fun get(index: Int): T = data[index]
    
    fun forEach(action: (T) -> Unit) = data.forEach(action)
    fun map(transform: (T) -> T): StableListWrapper<T> = 
        StableListWrapper(data.map(transform).toImmutableListSafe())
    
    companion object {
        fun <T> empty(): StableListWrapper<T> = StableListWrapper(persistentListOf())
    }
}

/**
 * Creates a stable wrapper for list state to improve Compose performance
 * 
 * @param list The list to wrap
 * @return StableListWrapper that helps avoid unnecessary recompositions
 */
@Composable
fun <T> rememberStableList(list: List<T>): StableListWrapper<T> {
    return remember(list) { StableListWrapper(list) }
}