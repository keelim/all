package com.keelim.arducon.ui.screen.qr

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.keelim.common.extensions.findActivity
import timber.log.Timber


@Composable
fun QrRoute(
    onShowBarcode: (String) -> Unit,
) {
    QrScreen(
        onShowBarcode = onShowBarcode
    )
}

@Composable
fun QrScreen(
    onShowBarcode: (String) -> Unit,
) {
    val context = LocalContext.current
    val scanner = rememberQrCodeScanner(
        context = context
    )
    LaunchedEffect(scanner) {
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                barcode.rawValue
                    ?.let {
                        onShowBarcode(it.toUri().toString())
                    } ?: onShowBarcode("No barcode found")
            }
            .addOnCanceledListener {
                // Task canceled
                context.findActivity().finish()
            }
            .addOnFailureListener { throwable ->
                // Task failed with an exception
                Timber.e(throwable)
                context.findActivity().finish()
            }
    }
}

@Composable
private fun rememberQrCodeScanner(
    context: Context = LocalContext.current
): GmsBarcodeScanner {
    return remember(context) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            )
            .enableAutoZoom() // available on 16.1.0 and higher
            .build()

        GmsBarcodeScanning.getClient(context, options)
    }
}
