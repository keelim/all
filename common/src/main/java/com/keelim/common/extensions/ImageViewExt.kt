package com.keelim.common.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load

fun ImageView.loadAsync(
  url: String?,
  @DrawableRes placeholder: Int?
) {
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

fun ImageView.loadAsync(
  uri: Uri?,
  @DrawableRes placeholder: Int?
) {
  if (uri == null) {
    placeholder?.let { load(it) }
  } else {
    load(uri) {
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