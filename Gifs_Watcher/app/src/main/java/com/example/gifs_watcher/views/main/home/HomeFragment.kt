package com.example.gifs_watcher.views.main.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.gifs_watcher.databinding.FragmentHomeBinding
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.views.main.MainViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var printedGif : Results = Results()

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    private lateinit var  gifUi : ImageView
    private lateinit var  backgroundGifUi : ImageView
    private lateinit var  gifTitle : TextView

    private lateinit var  likeGif : LottieAnimationView
    private lateinit var  dislikeGif : LottieAnimationView
    private lateinit var  starGif : LottieAnimationView
    private var isClicked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.println(Log.INFO,"debug","on create home")

        val mainViewModel_ = ViewModelProvider(this).get(MainViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        this.gifUi = binding.Gif
        this.backgroundGifUi = binding.backgroundGif
        this.gifTitle = binding.TitleGif

        mainViewModel.printedGifLD.observe(viewLifecycleOwner) {
            if (it != null) {
                printedGif = it
                gifTitle.setText(printedGif.contentDescription)

                try {
                    Glide.with(this)
                        .load(printedGif.media?.get(0)?.tinygif?.url)
                        .transform(MultiTransformation(CenterCrop(), FitCenter(), RoundedCorners(45)))
                        .into(this.gifUi)

                    Glide.with(this)
                        .load(printedGif.media?.get(0)?.gif?.url)
                        .apply(RequestOptions().centerCrop())
                        .transform(MultiTransformation(BlurTransformation(25, 4), CenterCrop(), FitCenter()))
                        .into(this.backgroundGifUi)
                } catch (e: Exception) {
                    Log.println(Log.ERROR,"debug","Gif create error : " + e.message)
                }
            }
        }

        initializeButtons()

        val root: View = binding.root
        return root
    }

    private fun initializeButtons() {
        this.likeGif = binding.likeFloatingActionButton
        this.dislikeGif = binding.deleteFloatingActionButton
        this.starGif = binding.starFloatingActionButton

        likeGif.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mainViewModel.getRandomGif(requireContext())
                isClicked = false
                likeGif.progress = 0f
            }
        })

        this.likeGif.setOnClickListener {
            if (!isClicked) {
                isClicked = true
                this.likeGif.setMinProgress(0.2f)
                this.likeGif.playAnimation()

            }
        }

        dislikeGif.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mainViewModel.getRandomGif(requireContext())
                isClicked = false
                dislikeGif.progress = 0f
            }
        })

        this.dislikeGif.setOnClickListener {
            if (!isClicked) {
                isClicked = true
                this.dislikeGif.playAnimation()
            }
        }

        starGif.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mainViewModel.getRandomGif(requireContext())
                isClicked = false
                starGif.progress = 0f
            }
        })

        this.starGif.setOnClickListener {
            if (!isClicked) {
                isClicked = true
                this.starGif.playAnimation()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}