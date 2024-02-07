package com.example.gifs_watcher.views.main.qrcode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gifs_watcher.R
import com.example.gifs_watcher.databinding.FragmentQrCodeScannerBinding
import com.example.gifs_watcher.views.main.MainViewModel
import pub.devrel.easypermissions.EasyPermissions
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.gifs_watcher.utils.managers.QrCodeManager
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.PermissionRequest
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Suppress("DEPRECATION")
class QrCodeScannerFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val cameraRequestCode = 123
    private val cameraPermission = android.Manifest.permission.CAMERA
    private lateinit var cameraExecutor: ExecutorService

    private var _binding: FragmentQrCodeScannerBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.println(Log.INFO, "debug", "on create Qr code Scanner")

        ViewModelProvider(this)[MainViewModel::class.java]
        _binding = FragmentQrCodeScannerBinding.inflate(inflater, container, false)

        binding.backFromScannerToChoose.setOnClickListener {
            findNavController().popBackStack(R.id.navigation_choose_method, false)
            findNavController().navigate(R.id.navigation_choose_method)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (hasCameraPermission()) {
            startCamera()
        } else {
            requestCameraPermission()
        }

        return binding.root
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraView.surfaceProvider)

                }

            // Configurer l'analyseur de QR Code
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor,
                        QrCodeManager(
                            findNavController(),
                            mainViewModel
                        )
                    )
                }


            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }


    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), cameraPermission)
    }

    private fun requestCameraPermission() {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, cameraRequestCode, cameraPermission)
                .setRationale("L'autorisation de la caméra est nécessaire pour scanner les QR codes.")
                .setPositiveButtonText("Autoriser")
                .setNegativeButtonText("Refuser")
                .build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Callback appelée lorsque les autorisations sont accordées
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        startCamera()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestCameraPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}