package com.example.gifs_watcher.views.main.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gifs_watcher.databinding.FragmentFriendsBinding
import com.example.gifs_watcher.views.main.MainViewModel
import com.example.gifs_watcher.R
import com.example.gifs_watcher.views.main.friends.adapters.FriendsAdapter
import com.example.gifs_watcher.views.main.friends.adapters.PendingRequesteAdapter
import com.example.gifs_watcher.views.main.friends.adapters.SentRequesteAdapter

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    private lateinit var searchBar : SearchView

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

        this.searchBar = this.binding.friendsSearchView
        searchBar.queryHint = "Recherchez un utilisateur"

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        setupFriendsAdapterForPendingRequest(view)
        setupFriendsAdapterForFriendsList(view)
        setupFriendsAdapterForSentRequest(view)

    }

    fun setupFriendsAdapterForPendingRequest(view: View){
        val rv = view.findViewById(R.id.rv_pending_request) as RecyclerView
        mainViewModel.getPendingFriendsUsers()
        mainViewModel.pendings.observe(viewLifecycleOwner) { response ->
            response?.let {
                val adapter = PendingRequesteAdapter(it)
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(this.context)
            }
        }
    }

    fun setupFriendsAdapterForFriendsList(view: View){
        val rv = view.findViewById(R.id.rv_friends) as RecyclerView
        mainViewModel.getFriendsUsers()
        mainViewModel.friends.observe(viewLifecycleOwner) { response ->
            response?.let {
                val adapter = FriendsAdapter(it) {

                }
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(this.context)
            }
        }
    }

    fun setupFriendsAdapterForSentRequest(view: View){
        val rv = view.findViewById(R.id.rv_sent_request) as RecyclerView
        mainViewModel.getSentFriendsUsers()
        mainViewModel.sents.observe(viewLifecycleOwner) { response ->
            response?.let {
                val adapter = SentRequesteAdapter(it)
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