package com.keelim.common.extensions

// inline fun <reified T> Intent.getParcel(key: String, clazz: Class<T>): T? = when {
//    SDK_INT >= 33 -> getParcelableExtra(key, clazz)
//    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
// }
//
// inline fun <reified T> Bundle.getParcel(key: String, clazz: Class<T>): T? = when {
//    SDK_INT >= 33 -> getParcelable(key, clazz)
//    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
// }
