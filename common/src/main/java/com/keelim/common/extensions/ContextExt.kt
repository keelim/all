package com.keelim.common.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

inline fun <reified T : Activity> Context.buildIntent(
    vararg argument: Pair<String, Any?>,
): Intent = Intent(this, T::class.java).apply {
    putExtras(bundleOf(*argument))
}

inline fun <reified T : Activity> Context.startActivity(
    vararg argument: Pair<String, Any?>,
) {
    startActivity(buildIntent<T>(*argument))
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

@Px
fun Context.dip(dipValue: Float) = (dipValue * resources.displayMetrics.density).toInt()

fun Context.getColorCompat(@ColorRes colorResId: Int) =
    ContextCompat.getColor(this, colorResId)

fun Context.getColorStateListCompat(@ColorRes colorResId: Int) =
    ContextCompat.getColorStateList(this, colorResId)

fun Context.startActivitySafely(intent: Intent) {
    if (intent.isValid(this)) {
        startActivity(intent)
    } else {
        toast("실행할 앱을 찾을 수 없습니다.")
    }
}

private fun Intent.isValid(ctx: Context): Boolean {
    return resolveActivity(ctx.packageManager) != null
}

fun Context.executeWeb(url: String?) {
    if (url == null) return
    startNonBrowserActivity(url, fallback = { startBrowserActivity(url) })
}

private fun Context.startBrowserActivity(url: String) {
//    CustomTabsIntent.Builder()
//        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
//        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
//        .setShowTitle(true)
//        .setStartAnimations(this, R.anim.fade_in, R.anim.fade_out)
//        .setExitAnimations(this, R.anim.fade_in, R.anim.fade_out)
//        .build()
//        .launchUrl(this, Uri.parse(url))
}

private fun Context.startNonBrowserActivity(url: String, fallback: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            fallback()
        }
    } else {
        fallback()
    }
}
