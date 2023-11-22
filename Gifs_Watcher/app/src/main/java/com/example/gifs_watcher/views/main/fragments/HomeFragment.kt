package com.example.gifs_watcher.views.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gifs_watcher.R
import com.example.gifs_watcher.databinding.FragmentHomeBinding
import com.example.gifs_watcher.viewmodel.MainViewModel
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.println(Log.INFO,"debug","on create home")



        val mainViewModel_ = ViewModelProvider(this).get(MainViewModel::class.java)

        try {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
        } catch (e: Exception) {
            Log.println(Log.ERROR,"debug","on create home error : " + e.message)
        }

        try {
            val Gif: ImageView = binding.Gif
            val backgroundGif: ImageView = binding.backgroundGif
            //Picasso.get().load(R.drawable.bobawooyo_dog_confused).into(Gif)
            Glide.with(this).load(R.drawable.bobawooyo_dog_confused).into(Gif)
            Glide.with(this).load(R.drawable.bobawooyo_dog_confused).centerCrop().into(backgroundGif)
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