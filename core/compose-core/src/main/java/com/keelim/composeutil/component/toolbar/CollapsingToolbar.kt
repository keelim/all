package com.keelim.composeutil.component.toolbar // package com.keelim.composeutil.component.toolbar
//
// import androidx.compose.foundation.layout.Box
// import androidx.compose.foundation.layout.PaddingValues
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.height
// import androidx.compose.foundation.lazy.rememberLazyListState
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.Stable
// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.saveable.mapSaver
// import androidx.compose.runtime.saveable.rememberSaveable
// import androidx.compose.runtime.setValue
// import androidx.compose.runtime.structuralEqualityPolicy
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.geometry.Offset
// import androidx.compose.ui.graphics.graphicsLayer
// import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
// import androidx.compose.ui.input.nestedscroll.NestedScrollSource
// import androidx.compose.ui.input.nestedscroll.nestedScroll
// import androidx.compose.ui.platform.LocalDensity
// import androidx.compose.ui.unit.dp
//
// private val MinToolbarHeight = 96.dp
// private val MaxToolbarHeight = 176.dp
//
// data class Sample private constructor(val uuid: String)
//
// @Stable
// interface ToolbarState {
//  val offset: Float
//  val height: Float
//  val progress: Float
//  val consumed: Float
//  var scrollTopLimitedReached: Boolean
//  var scrollOffset: Float
// }
//
// abstract class ScrollFlagState(heightRange: IntRange) : ToolbarState {
//  init {
//    require(heightRange.first >= 0 && heightRange.last >= heightRange.first) { "throw Exception" }
//  }
//
//  protected val minHeight = heightRange.first
//  protected val maxHeight = heightRange.last
//  protected val rangeDifference = heightRange.last - heightRange.first
//  protected var _consumed: Float = 0f
//  protected abstract var _scrollOffset: Float
//
//  override val height: Float
//    get() = (maxHeight - scrollOffset).coerceIn(minHeight.toFloat(), maxHeight.toFloat())
//
//  override val progress: Float
//    get() = 1 - (maxHeight - height) / rangeDifference
//
//  override val consumed: Float
//    get() = _consumed
//
//  override var scrollTopLimitedReached: Boolean = true
// }
//
// abstract class FixedScrollFlagState(heightRange: IntRange) : ScrollFlagState(heightRange) {
//  override val offset: Float = 0f
// }
//
// class ScrollState(heightRange: IntRange, scrollOffset: Float = 0f) :
//    FixedScrollFlagState(heightRange) {
//  override var _scrollOffset: Float by
//      mutableStateOf(
//          value = scrollOffset.coerceIn(0f, maxHeight.toFloat()),
//          policy = structuralEqualityPolicy())
//
//  override var scrollOffset: Float
//    get() = _scrollOffset
//    set(value) {
//      if (scrollTopLimitedReached) {
//        val oldOffset = _scrollOffset
//        _scrollOffset = value.coerceIn(0f, maxHeight.toFloat())
//        _consumed = oldOffset - _scrollOffset
//      } else {
//        _consumed = 0f
//      }
//    }
//
//  override val offset: Float
//    get() = -(scrollOffset - rangeDifference).coerceIn(0f, minHeight.toFloat())
//
//  companion object {
//    val Saver = run {
//      val minHeightKey = "MinHeight"
//      val maxHeightKey = "MaxHeight"
//      val scrollOffsetKey = "ScrollOffset"
//      mapSaver(
//          save = {
//            mapOf(
//                minHeightKey to it.minHeight,
//                maxHeightKey to it.maxHeight,
//                scrollOffsetKey to it.scrollOffset)
//          },
//          restore = {
//            ScrollState(
//                heightRange = (it[minHeightKey] as Int)..(it[maxHeightKey] as Int),
//                scrollOffset = it[scrollOffsetKey] as Float,
//            )
//          })
//    }
//  }
// }
//
// @Composable
// private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
//  return rememberSaveable(
//      saver = ScrollState.Saver
//  ) {
//    ScrollState(toolbarHeightRange)
//  }
// }
//
// @Composable
// fun Catalog(
//    items: List<Sample>,
//    columns: Int,
//    onClick1: () -> Unit,
//    onClick2: () -> Unit,
//    modifier: Modifier = Modifier
// ) {
//  val toolbarHeightRange: IntRange =
//      with(LocalDensity.current) { MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx() }
//  val toolbarState = rememberToolbarState(toolbarHeightRange = toolbarHeightRange)
//
//  val listState = rememberLazyListState()
//  val nestedScrollState = remember {
//    object : NestedScrollConnection {
//      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//        toolbarState.scrollTopLimitedReached =
//            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
//        toolbarState.scrollOffset -= available.y
//        return Offset(0f, toolbarState.consumed)
//      }
//    }
//  }
//
//  Box(modifier = modifier.nestedScroll(nestedScrollState)) {
//    LazyCatalog(
//        items = items,
//        columns = columns,
//        modifier =
//            Modifier.fillMaxSize().graphicsLayer {
//              translationY = toolbarState.height + toolbarState.offset
//            },
//        listState = listState,
//        contentPadding =
//            PaddingValues(
//                bottom = if (toolbarState is FixedScrollFlagState) MinToolbarHeight else 0.dp))
//    CollapsingToolbar(
//        progress = 1f,
//        onClick1 = onClick1,
//        onClick2 = onClick2,
//        modifier =
//            Modifier.fillMaxWidth()
//                .height(with(LocalDensity.current) { toolbarState.height.toDp() })
//                .graphicsLayer { translationY = toolbarState.offset })
//  }
// }
