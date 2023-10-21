package com.example.gifs_watcher.views.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.gifs_watcher.databinding.FragmentHomeBinding
import com.example.gifs_watcher.databinding.FragmentNotificationsBinding
import com.example.gifs_watcher.viewmodel.MainViewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mainViewModel_ =
            ViewModelProvider(this).get(MainViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications

        textView.text = mainViewModel_.getText(MainViewModel.View.PROFIL)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}