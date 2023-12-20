package com.example.gifs_watcher.views.main.qrcode

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.gifs_watcher.R
import com.example.gifs_watcher.databinding.FragmentChooseMethodBinding
import com.example.gifs_watcher.databinding.FragmentHomeBinding
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.views.main.MainViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class ChooseMethodFragment : Fragment() {

    private var _binding: FragmentChooseMethodBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.println(Log.INFO,"debug","on create home")

        val mainViewModel_ = ViewModelProvider(this).get(MainViewModel::class.java)
        _binding = FragmentChooseMethodBinding.inflate(inflater, container, false)


        binding.backFromChooseToHome.setOnClickListener {
            findNavController().navigate(R.id.navigation_home)
        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}