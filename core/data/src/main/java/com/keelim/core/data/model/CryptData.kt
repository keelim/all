@file:OptIn(ExperimentalEncodingApi::class)

package com.keelim.core.data.model

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@JvmInline
value class CryptData(val data: String)

fun String.encrypt(key: ByteArray, iv: ByteArray): Result<CryptData> = runCatching {
    val iv = IvParameterSpec(iv)
    val keySpec = SecretKeySpec(key, "AES")
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
    val crypted = cipher.doFinal(this.toByteArray())
    CryptData(Base64.encode(crypted))
}

fun CryptData.decrypt(key: ByteArray, iv: ByteArray): Result<String> = runCatching {
    val iv = IvParameterSpec(iv)
    val keySpec = SecretKeySpec(key, "AES")
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
    val original = cipher.doFinal(Base64.decode(data))
    String(original)
}
