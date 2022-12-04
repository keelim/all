package com.keelim.common.extensions

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import androidx.core.content.res.use
import androidx.core.graphics.applyCanvas
import androidx.core.text.PrecomputedTextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.core.view.drawToBitmap
import androidx.core.view.forEach
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

fun View.toInvisible() {
    visibility = View.INVISIBLE
}

fun View.toggleVisibility() {
    if (visibility == View.INVISIBLE) {
        toVisible()
    } else {
        toInvisible()
    }
}

fun Context.snack(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun Context.snack(view: View, @StringRes message: Int) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun ImageView.loadAsync(url: String?, @DrawableRes placeholder: Int? = null) {
    if (url == null) {
        placeholder?.let { load(it) }
    } else {
        load(url) {
            if (placeholder != null) {
                placeholder(placeholder)
            }
            crossfade(true)
        }
    }
}

fun ImageView.loadAsync(url: String?, doOnEnd: () -> Unit) {
    load(url) {
        listener(
            onSuccess = { _, _ -> doOnEnd() },
            onError = { _, _ -> doOnEnd() }
        )
    }
}

fun ImageView.setGrayscale(enable: Boolean) {
    colorFilter = if (enable) {
        ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    } else {
        null
    }
}

suspend fun Context.loadAsync(url: String): Bitmap? {
    val request = ImageRequest.Builder(this)
        .data(url)
        .build()
    val result = imageLoader.execute(request).drawable
    return (result as? BitmapDrawable)?.bitmap
}

fun TextView.asyncText(text: CharSequence?) {
    if (text == null) return
    if (this is AppCompatTextView) {
        val params = TextViewCompat.getTextMetricsParams(this)
        this.setTextFuture(PrecomputedTextCompat.getTextFuture(text, params, null))
    }
}

fun View.animateVisible(
    isVisible: Boolean,
    startDelay: Long = 0,
    duration: Long = 30,
) {
    animate()
        .setStartDelay(startDelay)
        .setDuration(duration)
        .alpha(if (isVisible) 1f else 0f)
        .withEndAction { this.isVisible = isVisible }
}

fun View.animateInvisible(
    isInvisible: Boolean,
    startDelay: Long = 0,
    duration: Long = 30,
) {
    animate()
        .setStartDelay(startDelay)
        .setDuration(duration)
        .alpha(if (isInvisible) 0f else 1f)
        .withEndAction { this.isInvisible = isInvisible }
}

fun View.animateGone(
    isGone: Boolean,
    startDelay: Long = 0,
    duration: Long = 300,
) {
    animate()
        .setStartDelay(startDelay)
        .setDuration(duration)
        .alpha(if (isGone) 0f else 1f)
        .withEndAction { this.isGone = isGone }
}

fun View.toCacheFile(folderName: String? = null, fileName: String): File {
    return drawToBitmap().toCacheFile(context, folderName = folderName, fileName = fileName)
}

fun Bitmap.toCacheFile(
    context: Context,
    folderName: String? = null,
    fileName: String,
): File {
    val cacheDir = if (folderName.isNullOrBlank()) {
        context.cacheDir
    } else {
        File(context.cacheDir, folderName).apply { mkdirs() }
    }
    return File(cacheDir, fileName).apply {
        FileOutputStream(this).use {
            compress(JPEG, 100, it)
            it.flush()
        }
    }
}

/**
 * An extension function which creates/retrieves a [SpringAnimation] and stores it in the [View]s
 * tag.
 */
// fun View.spring(
//    property: DynamicAnimation.ViewProperty,
//    stiffness: Float = 200f,
//    damping: Float = 0.3f,
//    startVelocity: Float? = null
// ): SpringAnimation {
//    val key = getKey(property)
//    var springAnim = getTag(key) as? SpringAnimation?
//    if (springAnim == null) {
//        springAnim = SpringAnimation(this, property).apply {
//            spring = SpringForce().apply {
//                this.dampingRatio = damping
//                this.stiffness = stiffness
//                startVelocity?.let { setStartVelocity(it) }
//            }
//        }
//        setTag(key, springAnim)
//    }
//    return springAnim
// }

/**
 * Map from a [ViewProperty] to an `id` suitable to use as a [View] tag.
 */
// @IdRes
// private fun getKey(property: DynamicAnimation.ViewProperty): Int {
//    return when (property) {
//        SpringAnimation.TRANSLATION_X -> R.id.translation_x
//        SpringAnimation.TRANSLATION_Y -> R.id.translation_y
//        SpringAnimation.TRANSLATION_Z -> R.id.translation_z
//        SpringAnimation.SCALE_X -> R.id.scale_x
//        SpringAnimation.SCALE_Y -> R.id.scale_y
//        SpringAnimation.ROTATION -> R.id.rotation
//        SpringAnimation.ROTATION_X -> R.id.rotation_x
//        SpringAnimation.ROTATION_Y -> R.id.rotation_y
//        SpringAnimation.X -> R.id.x
//        SpringAnimation.Y -> R.id.y
//        SpringAnimation.Z -> R.id.z
//        SpringAnimation.ALPHA -> R.id.alpha
//        SpringAnimation.SCROLL_X -> R.id.scroll_x
//        SpringAnimation.SCROLL_Y -> R.id.scroll_y
//        else -> throw IllegalAccessException("Unknown ViewProperty: $property")
//    }
// }

/**
 * Retrieve a color from the current [android.content.res.Resources.Theme].
 */
@ColorInt
fun Context.themeColor(
    @AttrRes themeAttrId: Int,
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

/**
 * Search this view and any children for a [ColorDrawable] `background` and return it's `color`,
 * else return `colorSurface`.
 */
@ColorInt
fun View.descendantBackgroundColor(): Int {
    val bg = backgroundColor()
    if (bg != null) {
        return bg
    } else if (this is ViewGroup) {
        forEach {
            val childBg = descendantBackgroundColorOrNull()
            if (childBg != null) {
                return childBg
            }
        }
    }
    return context.themeColor(android.R.attr.colorBackground)
}

@ColorInt
private fun View.descendantBackgroundColorOrNull(): Int? {
    val bg = backgroundColor()
    if (bg != null) {
        return bg
    } else if (this is ViewGroup) {
        forEach {
            val childBg = backgroundColor()
            if (childBg != null) {
                return childBg
            }
        }
    }
    return null
}

/**
 * Check if this [View]'s `background` is a [ColorDrawable] and if so, return it's `color`,
 * otherwise `null`.
 */
@ColorInt
fun View.backgroundColor(): Int? {
    val bg = background
    if (bg is ColorDrawable) {
        return bg.color
    }
    return null
}

/**
 * Walk up from a [View] looking for an ancestor with a given `id`.
 */
fun View.findAncestorById(@IdRes ancestorId: Int): View {
    return when {
        id == ancestorId -> this
        parent is View -> (parent as View).findAncestorById(ancestorId)
        else -> throw IllegalArgumentException("$ancestorId not a valid ancestor")
    }
}

/**
 * Potentially animate showing a [BottomNavigationView].
 *
 * Abruptly changing the visibility leads to a re-layout of main content, animating
 * `translationY` leaves a gap where the view was that content does not fill.
 *
 * Instead, take a snapshot of the view, and animate this in, only changing the visibility (and
 * thus layout) when the animation completes.
 */
fun BottomNavigationView.show() {
    if (visibility == VISIBLE) return

    val parent = parent as ViewGroup
    // View needs to be laid out to create a snapshot & know position to animate. If view isn't
    // laid out yet, need to do this manually.
    if (!isLaidOut) {
        measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.AT_MOST)
        )
        layout(parent.left, parent.height - measuredHeight, parent.right, parent.height)
    }

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    drawable.setBounds(left, parent.height, right, parent.height + height)
    parent.overlay.add(drawable)
    ValueAnimator.ofInt(parent.height, top).apply {
        startDelay = 100L
        duration = 300L
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.linear_out_slow_in
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
            visibility = VISIBLE
        }
        start()
    }
}

/**
 * Potentially animate hiding a [BottomNavigationView].
 *
 * Abruptly changing the visibility leads to a re-layout of main content, animating
 * `translationY` leaves a gap where the view was that content does not fill.
 *
 * Instead, take a snapshot, instantly hide the view (so content lays out to fill), then animate
 * out the snapshot.
 */
fun BottomNavigationView.hide() {
    if (visibility == GONE) return

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    val parent = parent as ViewGroup
    drawable.setBounds(left, top, right, bottom)
    parent.overlay.add(drawable)
    visibility = GONE
    ValueAnimator.ofInt(top, parent.height).apply {
        startDelay = 100L
        duration = 200L
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.fast_out_linear_in
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
        }
        start()
    }
}

/**
 * A copy of the KTX method, adding the ability to add extra padding the bottom of the [Bitmap];
 * useful when it will be used in a [android.graphics.BitmapShader][BitmapShader] with
 * a [android.graphics.Shader.TileMode.CLAMP][CLAMP tile mode].
 */
fun View.drawToBitmap(@Px extraPaddingBottom: Int = 0): Bitmap {
    if (!ViewCompat.isLaidOut(this)) {
        throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
    }
    return Bitmap.createBitmap(width, height + extraPaddingBottom, Bitmap.Config.ARGB_8888)
        .applyCanvas {
            translate(-scrollX.toFloat(), -scrollY.toFloat())
            draw(this)
        }
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

interface ViewHolderLifecycleInitializer {
    var lifecycleOwner: LifecycleOwner?
    fun initialize(itemView: View) {
        itemView.doOnAttach {
            lifecycleOwner = itemView.findViewTreeLifecycleOwner()
        }
        itemView.doOnDetach {
            lifecycleOwner = null
        }
    }
}
