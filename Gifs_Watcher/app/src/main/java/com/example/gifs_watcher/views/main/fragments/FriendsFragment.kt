package com.example.gifs_watcher.views.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gifs_watcher.databinding.FragmentFriendsBinding
import com.example.gifs_watcher.viewmodel.MainViewModel
import com.example.gifs_watcher.R
import com.example.gifs_watcher.utils.adapters.FriendsAdapter

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFriendsAdapter(view)

    }

    fun setupFriendsAdapter(view: View){
        val rv = view.findViewById(R.id.rv_pending_request) as RecyclerView
        mainViewModel.listFriend.value = ArrayList() // je sais pas ou l'ecrire mais est super important sinon on peut pas intégrer des données dans le livedata, une sorte d'initialisation
        mainViewModel.getUsers()
        mainViewModel.Friends.observe(viewLifecycleOwner) { response ->
            response?.let {
                val adapter = FriendsAdapter(it)
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