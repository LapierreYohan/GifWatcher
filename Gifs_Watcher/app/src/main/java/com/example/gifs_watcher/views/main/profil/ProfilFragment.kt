package com.example.gifs_watcher.views.main.profil

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.example.gifs_watcher.R
import com.example.gifs_watcher.cache.CacheDatasource
import com.example.gifs_watcher.databinding.FragmentProfilBinding
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.views.main.profil.adapters.LikesAdapter
import com.example.gifs_watcher.views.main.MainViewModel
import com.example.gifs_watcher.views.main.profil.menu.ParameterMenu
import jp.wasabeef.glide.transformations.BlurTransformation

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    private lateinit var  profilGif : ImageView
    private lateinit var  userName : TextView
    private lateinit var  bio : TextView

    private lateinit var  parameterMenu : ParameterMenu

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mainViewModel_ =
            ViewModelProvider(this).get(MainViewModel::class.java)

        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val ProfilUser: User? = mainViewModel.getProfil()

        this.profilGif = binding.profilPicture
        this.userName = binding.tvUserName
        this.bio = binding.tvBio

        this.userName.setText(ProfilUser?.displayname)
        this.bio.setText(ProfilUser?.bio)

        this.parameterMenu = ParameterMenu(requireContext(), binding.profilParameters, mainViewModel)

        try {
            Glide.with(this)
                .load(ProfilUser?.profilPicture)
                .apply(RequestOptions().centerCrop())
                .transform(
                    MultiTransformation(CenterCrop(), FitCenter())
                )
                .into(this.profilGif)
        } catch (e: Exception) {
            Log.println(Log.ERROR, "debug", "Gif create error : " + e.message)
        }

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProfilAdapterForLike(view)
    }

    fun setupProfilAdapterForLike(view: View){
        val rv = view.findViewById(R.id.rv_like) as RecyclerView
        mainViewModel.getLikesProfil(context?.applicationContext!!)
        mainViewModel.likes.observe(viewLifecycleOwner) { response ->
            response?.let {
                val adapter = LikesAdapter(it){

                }
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(this.context)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}