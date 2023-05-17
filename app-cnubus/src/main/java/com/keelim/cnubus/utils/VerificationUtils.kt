/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import android.text.TextUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 루팅, 위변조, 구글 플레이 확인 하는 방법
 */
@Singleton
class VerificationUtils @Inject constructor(
    @ApplicationContext val ctx: Context,
) {

    private val rootFiles = arrayOf(
        "/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/system/usr/we-need-root/",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su",
        "/su/bin/su",
        "/su/bin",
        "/system/xbin/daemonsu",
        "/system/su",
        "/system/sbin/su",
        "/system/xbin/mu",
        "/system/bin/.ext/.su",
        "/system/usr/su-backup",
        "/data/data/com.noshufou.android.su",
        "/system/app/su.apk",
        "/system/bin/.ext",
        "/system/xbin/.ext",
        "/su/bin/su",
    )

    private val rootPackages = arrayOf(
        "com.devadvance.rootcloak",
        "com.devadvance.rootcloakplus",
        "com.koushikdutta.superuser",
        "com.thirdparty.superuser",
        "eu.chainfire.supersu",
        "de.robv.android.xposed.installer",
        "com.saurik.substrate",
        "com.zachspong.temprootremovejb",
        "com.amphoras.hidemyroot",
        "com.amphoras.hidemyrootadfree",
        "com.formyhm.hiderootPremium",
        "com.formyhm.hideroot",
        "com.noshufou.android.su",
        "com.noshufou.android.su.elite",
        "com.yellowes.su",
        "com.topjohnwu.magisk",
        "com.kingroot.kinguser",
        "com.kingo.root",
        "com.smedialink.oneclickroot",
        "com.zhiqupk.root.global",
        "com.alephzain.framaroot",
        "cn.luomao.gamekiller",
        "cn.maocai.gamekiller",
        "cn.mc.sq",
        "com.cih.game_cih",
        "com.cih.gamecih",
        "com.cih.gamecih2",
        "cris.jeong.samsung",
        "cris1.jeong1.samsung1",
        "cris2.jeong2.samsung2",
        "idv.aqua.buildlog",
        "lg.min.cris",
        "samsung.cris.jeong",
        "test.aaa.1",
        "test.bbb.2",
    )

    private val runtime by lazy {
        Runtime.getRuntime()
    }

    fun isVerifiedDebug(): Boolean = checkRootFiles() ||
        checkSUExist() ||
        checkRootPackages() ||
        buildTagCheck()

    fun isVerifiedRelease(): Boolean = isVerifiedDebug() ||
        isNotInstalledViaGooglePlay()

    private fun checkRootFiles(): Boolean {
        for (path in rootFiles) {
            try {
                if (File(path).exists()) {
                    return true
                }
            } catch (e: RuntimeException) {
                continue
            }
        }
        return false
    }

    private fun checkSUExist(): Boolean {
        var process: Process? = null
        val su = arrayOf(
            "/system/xbin/which",
            "su",
        )
        try {
            process = runtime.exec(su)
            BufferedReader(
                InputStreamReader(
                    process.inputStream,
                    Charset.forName("UTF-8"),
                ),
            ).use { reader -> return reader.readLine() != null }
        } catch (_: IOException) {
        } catch (_: Exception) {
        } finally {
            process?.destroy()
        }
        return false
    }

    private fun checkRootPackages(): Boolean {
        val pm = ctx.packageManager
        if (pm != null) {
            for (pkg in rootPackages) {
                try {
                    pm.getPackageInfo(pkg, 0)
                    return true
                } catch (ignored: PackageManager.NameNotFoundException) {
                    // fine, package doesn't exist.
                }
            }
        }
        return false
    }

    private fun buildTagCheck(): Boolean {
        val tags = Build.TAGS ?: return true
        if (tags.contains("test-keys") || tags.contains("test")) {
            return true
        }
        return false
    }

    private fun isNotInstalledViaGooglePlay(): Boolean {
        return isInstalledVia(ctx, GOOGLE_PLAY).not()
    }

    private fun isInstalledViaAmazon(): Boolean {
        return isInstalledVia(ctx, AMAZON)
    }

    private fun isSideloaded(ctx: Context): Boolean {
        val installer = getInstallerPackageName(ctx)
        return TextUtils.isEmpty(installer)
    }

    private fun isInstalledVia(ctx: Context, required: String): Boolean {
        val installer = getInstallerPackageName(ctx)
        return required == installer
    }

    private fun getInstallerPackageName(ctx: Context): String? {
        try {
            val packageName = ctx.packageName
            val pm = ctx.packageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val info = pm.getInstallSourceInfo(packageName)
                return info.installingPackageName
            }
            return pm.getInstallerPackageName(packageName)
        } catch (_: NameNotFoundException) {
        }
        return ""
    }

    companion object {
        const val GOOGLE_PLAY = "com.android.vending"
        const val AMAZON = "com.amazon.venezia"
    }
}
