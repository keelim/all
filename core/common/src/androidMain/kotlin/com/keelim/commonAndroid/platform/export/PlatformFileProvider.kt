package com.keelim.commonAndroid.platform.export

import androidx.core.content.FileProvider

/** Separate manifest identity for platform exports, preserving app-specific image providers. */
class PlatformFileProvider : FileProvider()
