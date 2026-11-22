package com.keelim.commonAndroid.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import jakarta.inject.Inject
import timber.log.Timber

class AppSignatureHashUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val appSignatures: List<String>
        get() = runCatching {
            resolveSignatures().mapNotNull { signature ->
                hash(context.packageName, signature.toCharsString())
            }
        }.getOrElse { throwable ->
            Timber.e(throwable, "Unable to resolve app signatures")
            emptyList()
        }

    private fun resolveSignatures(): List<Signature> {
        val packageManager = context.packageManager
        val packageName = context.packageName
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES,
            )
            val signingInfo = packageInfo.signingInfo ?: return emptyList()
            val signatures = if (signingInfo.hasMultipleSigners()) {
                signingInfo.apkContentsSigners
            } else {
                signingInfo.signingCertificateHistory
            }
            signatures?.toList().orEmpty()
        } else {
            @Suppress("DEPRECATION")
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES,
            )
            @Suppress("DEPRECATION")
            packageInfo.signatures?.toList().orEmpty()
        }
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
        }.onFailure { Timber.e(it, "Unable to hash app signature") }
            .getOrNull()
    }

    companion object {
        private const val HASH_TYPE = "SHA-256"
        const val NUM_HASHED_BYTES = 9
        const val NUM_BASE64_CHAR = 11
    }
}
