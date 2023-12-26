package com.example.gifs_watcher.utils.managers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.gifs_watcher.views.main.MainViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class QrCodeManager (
    private val context: Context,
    private val navController: NavController,
    private val mainViewModel: MainViewModel
): ImageAnalysis.Analyzer {


    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {

        val img = image.image
        if (img != null) {

            val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

            // Process image searching for barcodes
            val options = BarcodeScannerOptions.Builder()
                .build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        for (barcode in barcodes) {
                            if (mainViewModel.seeGifTraitement) {
                                return@addOnSuccessListener
                            }
                            val gifId = barcode.rawValue.toString()
                            mainViewModel.seeGif(gifId, navController)
                        }
                    }
                }
                .addOnFailureListener { }
        }

        image.close()
    }
}

