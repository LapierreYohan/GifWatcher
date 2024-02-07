package com.example.gifs_watcher.views.main.qrcode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gifs_watcher.R
import com.example.gifs_watcher.databinding.FragmentChooseMethodBinding
import com.example.gifs_watcher.views.main.MainViewModel

class ChooseMethodFragment : Fragment() {

    private var _binding: FragmentChooseMethodBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.println(Log.INFO, "debug", "on create Choosen method")

        ViewModelProvider(this)[MainViewModel::class.java]
        _binding = FragmentChooseMethodBinding.inflate(inflater, container, false)
        binding.qrcodeCodeTextinput.error = null
        binding.qrcodeCodeTextinputLayout.error = null
        binding.qrcodeCodeTextinput.setText("")

        binding.backFromChooseToHome.setOnClickListener {
            findNavController().popBackStack(R.id.navigation_home, false)
            findNavController().navigate(R.id.navigation_home)
        }

        binding.qrcodeOptionLayout.setOnClickListener {
            findNavController().navigate(R.id.navigation_qrcode_scanner)
        }

        binding.chooseCodeButton.setOnClickListener {
            mainViewModel.seeGif(binding.qrcodeCodeTextinput.text.toString(), findNavController())
        }

        mainViewModel.gifResponse.observe(viewLifecycleOwner) {

            binding.qrcodeCodeTextinput.error = null
            binding.qrcodeCodeTextinputLayout.error = null
            if (it.failed()) {
                it.error().forEach { gifError ->
                    binding.qrcodeCodeTextinputLayout.error = gifError?.message
                    binding.qrcodeCodeTextinput.setError(gifError?.message, null)
                    binding.qrcodeCodeTextinput.setText("")
                }
            }
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}