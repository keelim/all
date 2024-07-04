package com.keelim.common.extensions

import android.content.Context
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
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.Px
import androidx.core.content.res.use
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.core.view.drawToBitmap
import androidx.core.view.forEach
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import java.io.File
import java.io.FileOutputStream

infix fun View.visibleIf(condition: Boolean) =
    run { visibility = if (condition) VISIBLE else GONE }

infix fun View.goneIf(condition: Boolean) =
    run { visibility = if (condition) GONE else VISIBLE }

infix fun View.invisibleIf(condition: Boolean) =
    run { visibility = if (condition) View.INVISIBLE else VISIBLE }

fun View.toVisible() {
    visibility = VISIBLE
}

fun View.toGone() {
    visibility = GONE
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
            onError = { _, _ -> doOnEnd() },
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
        intArrayOf(themeAttrId),
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
