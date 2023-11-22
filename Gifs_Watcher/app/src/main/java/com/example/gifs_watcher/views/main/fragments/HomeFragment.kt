package com.example.gifs_watcher.views.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.gifs_watcher.R
import com.example.gifs_watcher.databinding.FragmentHomeBinding
import com.example.gifs_watcher.viewmodel.MainViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.println(Log.INFO,"debug","on create home")

        val mainViewModel_ = ViewModelProvider(this).get(MainViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        try {
            val Gif: ImageView = binding.Gif
            val backgroundGif: ImageView = binding.backgroundGif
            Glide.with(this)
                .load(R.drawable.bobawooyo_dog_confused)
                .transform(MultiTransformation(RoundedCorners(25)))
                .into(Gif)
            Glide.with(this)
                .load("https://media.tenor.com/5_E52aMtD4EAAAAd/bobawooyo-dog-confused.gif")
                .transform(MultiTransformation(CenterCrop(),BlurTransformation(25, 4),))
                .into(backgroundGif)
        }catch (e: Exception) {
            Log.println(Log.ERROR,"debug","Gif create error : " + e.message)
        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}