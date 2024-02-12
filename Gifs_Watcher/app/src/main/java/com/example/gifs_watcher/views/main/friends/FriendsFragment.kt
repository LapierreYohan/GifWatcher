package com.example.gifs_watcher.views.main.friends

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gifs_watcher.databinding.FragmentFriendsBinding
import com.example.gifs_watcher.views.main.MainViewModel
import com.example.gifs_watcher.R
import com.example.gifs_watcher.views.main.friends.adapters.FriendsAdapter
import com.example.gifs_watcher.views.main.friends.adapters.PendingRequesteAdapter
import com.example.gifs_watcher.views.main.friends.adapters.SentRequestAdapter
import com.example.gifs_watcher.views.main.friends.popUp.AddFriendsPopup
import timber.log.Timber

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

        val showPopUp = AddFriendsPopup("Add a Friend")

        showPopUp.addedFriend.observe(viewLifecycleOwner) { response ->
            response?.let {
                mainViewModel.requestFriend(it)
            }
        }

        mainViewModel.addedFriendResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                showPopUp.addedFriendResponse.postValue(it)
            }
        }

        binding.addFriendButton.setOnClickListener {
            showPopUp.show((context as AppCompatActivity).supportFragmentManager, "Add_friends_popup")
        }

        setupFriendsAdapterForPendingRequest(root)
        setupFriendsAdapterForFriendsList(root)
        setupFriendsAdapterForSentRequest(root)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.searchBar = this.binding.friendsSearchView
        searchBar.queryHint = "Enter a username"

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupFriendsAdapterForPendingRequest(view: View){

        val rv = view.findViewById(R.id.rv_pending_request) as RecyclerView
        val pendingTitle = view.findViewById<TextView>(R.id.pending_request_txt)

        mainViewModel.pendings.observe(viewLifecycleOwner) { response ->
            Timber.e("pendings: $response")
            response?.let { it ->
                pendingTitle.text = "Pending Request - ${it.size}"
                val adapter = PendingRequesteAdapter(it, pendingTitle)
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(this.context)

                adapter.acceptFriend.observe(viewLifecycleOwner) { response ->
                    response?.let {username ->
                        mainViewModel.acceptFriend(username)
                    }
                }

                adapter.denyFriend.observe(viewLifecycleOwner) { response ->
                    response?.let {username ->
                        mainViewModel.denyFriend(username)
                    }
                }
            }
        }

        mainViewModel.getPendingFriendsUsers()
    }

    @SuppressLint("SetTextI18n")
    private fun setupFriendsAdapterForFriendsList(view: View){

        val rv = view.findViewById(R.id.rv_friends) as RecyclerView
        val friendTitle = view.findViewById<TextView>(R.id.friends_txt)

        mainViewModel.friends.observe(viewLifecycleOwner) { response ->
            Timber.e("friends: $response")
            response?.let {
                friendTitle.text = "Friends - ${it.size}"
                val adapter = FriendsAdapter(it)
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(this.context)

                adapter.deleteFriend.observe(viewLifecycleOwner) { response ->
                    response?.let {username ->
                        mainViewModel.deleteFriend(username)
                    }
                }
            }
        }

        mainViewModel.getFriendsUsers()
    }

    @SuppressLint("SetTextI18n")
    private fun setupFriendsAdapterForSentRequest(view: View){

        val rv = view.findViewById(R.id.rv_sent_request) as RecyclerView
        val entTitle = view.findViewById<TextView>(R.id.sent_request_txt)

        mainViewModel.sents.observe(viewLifecycleOwner) { response ->
            Timber.e("sents: $response")
            response?.let {
                entTitle.text = "Request Sent - ${it.size}"
                val adapter = SentRequestAdapter(it, entTitle)
                rv.adapter = adapter
                rv.layoutManager = LinearLayoutManager(this.context)

                adapter.cancelRequest.observe(viewLifecycleOwner) { response ->
                    response?.let {username ->
                        mainViewModel.cancelRequest(username)
                    }
                }
            }
        }

        mainViewModel.getSentFriendsUsers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}