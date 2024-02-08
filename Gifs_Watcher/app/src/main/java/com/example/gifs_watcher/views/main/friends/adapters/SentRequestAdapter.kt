package com.example.gifs_watcher.views.main.friends.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.gifs_watcher.R
import com.example.gifs_watcher.models.FriendRequest
import com.example.gifs_watcher.utils.enums.FriendPopUpType
import com.example.gifs_watcher.views.main.friends.popUp.FriendsPopup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SentRequestAdapter(private val users: ArrayList<FriendRequest>, private val title : TextView) : RecyclerView.Adapter<SentRequestAdapter.ItemViewHolder>() {

    private val _cancelRequest : MutableLiveData<String> = MutableLiveData()
    val cancelRequest : LiveData<String> = _cancelRequest

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_title)
        val descView: TextView = itemView.findViewById(R.id.tv_subTitle)
        val card: ConstraintLayout = itemView.findViewById(R.id.friend_card)
        val gif: ImageView = itemView.findViewById(R.id.Iv_preview)
        val action : ImageView = itemView.findViewById(R.id.action_delete_SR)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.sent_request_card_list, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.println(Log.INFO,"debug","onBindViewHolder open" )
        val item = users[position]
        Log.println(Log.INFO,"debug", "onBindViewHolder item : $item")

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.card.context, "Clicked: ${item.displayDest}", Toast.LENGTH_SHORT).show()
        }
        holder.action.setOnClickListener {
            val showPopUp = FriendsPopup(item, FriendPopUpType.DELETE_SENT, "Cancel your request", "Do you really want to cancel your friend request to ${item.displayDest} ?")

            showPopUp.cancelRequest.observeForever { response ->
                response?.let {
                    _cancelRequest.postValue(it)
                }
            }

            showPopUp.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "Friends_popup")
        }

        holder.textView.text = item.displayDest

        val date: Date = item.timestamp?.toDate() ?: Date()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate: String = dateFormatter.format(date)

        holder.descView.text = item.dest + " - " + formattedDate
        try {
            // Loading main gif
            Glide.with(holder.itemView.context)
                .load(item.displayDestAvatar)
                .transform(MultiTransformation(CenterCrop(), FitCenter(), RoundedCorners(90)))
                .into(holder.gif)

        } catch (e: Exception) {
            Log.println(Log.ERROR,"debug","Gif create error : " + e.message)
        }
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return users.size  // vraiment pas sur de ca
    }

}