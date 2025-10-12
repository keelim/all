package com.keelim.commonAndroid.util

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.inject.Inject

class AppSignatureHashUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val appSignatures: List<String>
        get() = runCatching {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            packageInfo.signatures?.mapNotNull { signature ->
                hash(context.packageName, signature.toCharsString())
            } ?: emptyList()
        }.getOrElse { throwable ->
            throwable.logError()
            emptyList()
        }

    private fun hash(packageName: String, signature: String): String? {
        val appInfo = "$packageName $signature"
        return runCatching {
            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
            messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
            val hashSignature = messageDigest.digest()
                .copyOfRange(0, NUM_HASHED_BYTES)
            val base64Hash =
                Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
                    .take(NUM_BASE64_CHAR)
            base64Hash
        }.onFailure { it.logError() }
            .getOrNull()
    }

    companion object {
        private const val HASH_TYPE = "SHA-256"
        const val NUM_HASHED_BYTES = 9
        const val NUM_BASE64_CHAR = 11
    }
}
