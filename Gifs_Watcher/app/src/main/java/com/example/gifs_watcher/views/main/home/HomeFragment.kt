package com.example.gifs_watcher.views.main.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
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
import com.example.gifs_watcher.databinding.FragmentHomeBinding
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.views.main.MainViewModel
import com.example.gifs_watcher.views.main.qrcode.ChooseMethodFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.wasabeef.glide.transformations.BlurTransformation
import timber.log.Timber
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var printedGif : Results = Results()

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    private var searchTheme : String = ""

    private lateinit var  gifUi : ImageView
    private lateinit var  backgroundGifUi : ImageView
    private lateinit var  searchBar : SearchView

    private lateinit var  likeGif : LottieAnimationView
    private lateinit var  dislikeGif : LottieAnimationView
    private lateinit var  starGif : LottieAnimationView

    private var isClicked : Boolean = false
    private var areAdditionalInfoVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.println(Log.INFO,"debug","on create home")

        val mainViewModel_ = ViewModelProvider(this).get(MainViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        this.searchBar = binding.mainSearchView
        this.searchBar.queryHint = "Enter a Gif theme"

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

        binding.floatingActionButton3.setOnClickListener {

            findNavController().navigate(R.id.navigation_choose_method)
        }

        val moreInfoButton: FloatingActionButton = binding.moreInformationsFloatingActionButton

        moreInfoButton.setOnClickListener {
            areAdditionalInfoVisible = !areAdditionalInfoVisible
            updateAdditionalInfoVisibility()
            updateMoreInfoButtonIcon(moreInfoButton)
        }

        this.gifUi = binding.Gif
        this.backgroundGifUi = binding.backgroundGif

        mainViewModel.printedGifA.observe(viewLifecycleOwner) {
            if (it != null) {
                printedGif = it

                updateAdditionalInfoVisibility()
                updateMoreInfoButtonIcon(moreInfoButton)

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

    private fun updateAdditionalInfoVisibility() {
        var componentsArray = arrayListOf(
            binding.alineaPicture,
            binding.iconPicture,
            binding.pictureDims,

            binding.alineaTaille,
            binding.iconTaille,
            binding.pictureTaille,

            binding.alineaTemps,
            binding.iconTemps,
            binding.pictureTemps,

            binding.iconDuration,
            binding.pictureDuration,
        )

        val fullText = printedGif.contentDescription?.trim() ?: ""

        var textSize = 24f
        var maxLength = 50

        if (areAdditionalInfoVisible) {
            textSize = 16f
            maxLength = 80
        }

        val minTextSize = 20f
        val maxTextSize = 25f
        textSize = textSize.coerceIn(minTextSize, maxTextSize)

        binding.TitleGif.textSize = textSize

        if (fullText.length > maxLength) {
            val truncatedText = "${fullText.substring(0, maxLength - 6)}..."
            binding.TitleGif.text = truncatedText
        } else {
            binding.TitleGif.text = fullText
        }

        val dimsText = StringBuilder().apply {
            printedGif.media?.get(0)?.gif?.dims?.get(1)?.let { append(it) }
            append("x")
            printedGif.media?.get(0)?.gif?.dims?.get(0).let { append(it) }
        }.toString()

        binding.pictureDims.text = dimsText

        val sizeText = StringBuilder().apply {
            printedGif.media?.get(0)?.gif?.size?.let {
                val sizeInKb = it / 1000
                val formattedSize = NumberFormat.getNumberInstance(Locale.getDefault()).format(sizeInKb)
                append(formattedSize)
            }
            append(" Kb")
        }.toString()

        binding.pictureTaille.text = sizeText

        val dateText = StringBuilder().apply {
            printedGif.created?.let {
                val timestamp = it.toLong() * 1000 // Convertir en millisecondes
                val date = Date(timestamp)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                append(dateFormat.format(date))
            }
        }.toString()

        binding.pictureTemps.text = dateText

        val durationText = StringBuilder().apply {
            printedGif.media?.get(0)?.gif?.duration?.let {
                append(it)
                append("s")
            }
        }.toString()

        binding.pictureDuration.text = durationText

        for (component in componentsArray) {
            component.visibility = if (areAdditionalInfoVisible) View.VISIBLE else View.GONE
        }
    }

    private fun updateMoreInfoButtonIcon(button: FloatingActionButton) {
        val iconResId = if (areAdditionalInfoVisible) R.drawable.up_arrow else R.drawable.down_arrow
        button.setImageResource(iconResId)
    }
}