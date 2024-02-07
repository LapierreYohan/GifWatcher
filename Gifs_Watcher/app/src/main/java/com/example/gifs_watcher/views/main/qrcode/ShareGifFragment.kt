package com.example.gifs_watcher.views.main.qrcode

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gifs_watcher.R
import com.example.gifs_watcher.databinding.FragmentShareGifBinding
import com.example.gifs_watcher.views.main.MainViewModel
import io.github.g0dkar.qrcode.QRCode
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
class ShareGifFragment : Fragment() {

    private var _binding: FragmentShareGifBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var bitmap : Bitmap

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.println(Log.INFO, "debug", "on create QrCode")

        ViewModelProvider(this)[MainViewModel::class.java]
        _binding = FragmentShareGifBinding.inflate(inflater, container, false)

        binding.backFromShareToHome.setOnClickListener {
            findNavController().popBackStack(R.id.navigation_home, false)
            findNavController().navigate(R.id.navigation_home)
        }

        mainViewModel.sharedGif.observe(viewLifecycleOwner) {
            binding.shareCodeTextinput.setText(it?.id)
            binding.shareOptionTitle.text = it?.contentDescription

            val qrCode = QRCode(it?.id!!).render(60, margin = 60)
            val bitmapArray = ByteArrayOutputStream().also { array ->
                qrCode.writeImage(array)
            }.toByteArray()
            bitmap = BitmapFactory.decodeStream(bitmapArray.inputStream())
            binding.shareOptionImage.setImageBitmap(bitmap)
        }

        binding.shareCodeButton.setOnClickListener {

            val constraintLayout = binding.shareOptionLayout
            constraintLayout.isDrawingCacheEnabled = true
            val bitmapComponent = Bitmap.createBitmap(constraintLayout.drawingCache)

            mainViewModel.downloadQrCode(
                requireContext(),
                bitmapComponent,
                binding.shareCodeTextinput.text.toString()
            )
            findNavController().popBackStack(R.id.navigation_home, false)
            findNavController().navigate(R.id.navigation_home)
        }

        val sharedCodeInput = binding.shareCodeTextinput
        sharedCodeInput.showSoftInputOnFocus = false

        sharedCodeInput.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                mainViewModel.copyIdGif(
                    requireContext(),
                    binding.shareCodeTextinput.text.toString()
                )
            }
            true
        }

        mainViewModel.getShareGif()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}