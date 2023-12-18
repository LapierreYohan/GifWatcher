package com.example.gifs_watcher.views.main.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.wasabeef.glide.transformations.BlurTransformation

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var printedGif : Results = Results()

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    private var searchTheme : String = ""

    private lateinit var  gifUi : ImageView
    private lateinit var  backgroundGifUi : ImageView
    private lateinit var  gifTitle : TextView
    private lateinit var  searchBar : SearchView

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

        this.searchBar = binding.mainSearchView
        this.searchBar.queryHint = "Recherchez un GIF"

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchTheme = query ?: ""
                searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchTheme = newText ?: ""
                return true
            }
        })

        this.gifUi = binding.Gif
        this.backgroundGifUi = binding.backgroundGif
        this.gifTitle = binding.TitleGif

        mainViewModel.printedGifA.observe(viewLifecycleOwner) {
            if (it != null) {
                printedGif = it

                val fullText = printedGif.contentDescription ?: ""
                val maxLength = 35

                val minTextSize = 21f
                val maxTextSize = 25f

                val textSize = when {
                    fullText.length > maxLength -> minTextSize
                    fullText.length > maxLength - 10 -> ((maxTextSize - minTextSize) / 10 * (maxLength - 10 - fullText.length)) + minTextSize
                    else -> maxTextSize
                }

                gifTitle.textSize = textSize

                if (fullText.length > maxLength) {
                    val truncatedText = "${fullText.substring(0, maxLength - 6)}..."

                    gifTitle.text = truncatedText
                } else {
                    gifTitle.text = fullText
                }

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

        val likeAnimator = createGifAnimator(likeGif, 0.35f, 0.7f, 1600)
        val dislikeAnimator = createGifAnimator(dislikeGif, 0f, 0.7f, 600)
        val starAnimator = createGifAnimator(starGif, 0.25f, 0.7f, 1300)

        likeGif.setOnClickListener {

            mainViewModel.likeGif()
            startGifAnimation(likeAnimator)
        }

        dislikeGif.setOnClickListener {
            mainViewModel.dislikeGif()
            startGifAnimation(dislikeAnimator)
        }

        starGif.setOnClickListener {
            mainViewModel.starGif()
            startGifAnimation(starAnimator)
        }
    }

    private fun createGifAnimator(fab: LottieAnimationView, startFraction: Float, endFraction: Float, duration : Long): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(fab, "progress", 0f, 1f)
        animator.duration = duration

        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            if (progress >= endFraction && isClicked) {
                mainViewModel.getRandomGif(requireContext(), searchTheme)
                isClicked = false
            }
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animator.setCurrentFraction(startFraction)
            }
        })

        return animator.apply {
            setCurrentFraction(startFraction)
        }
    }

    private fun startGifAnimation(animator: ObjectAnimator) {
        if (!isClicked) {
            isClicked = true
            animator.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}