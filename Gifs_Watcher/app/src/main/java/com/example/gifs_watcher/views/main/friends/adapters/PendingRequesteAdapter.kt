package com.example.gifs_watcher.views.main.friends.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.gifs_watcher.R
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.utils.enums.FriendPopUpType
import com.example.gifs_watcher.views.main.friends.popUp.FriendsPopup


class PendingRequesteAdapter(private val users: ArrayList<User?>?) : RecyclerView.Adapter<PendingRequesteAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_title)
        val descView: TextView = itemView.findViewById(R.id.tv_subTitle)
        val card: ConstraintLayout = itemView.findViewById(R.id.friend_card)
        val gif: ImageView = itemView.findViewById(R.id.Iv_preview)
        val actionValide : ImageView = itemView.findViewById(R.id.action_valide_PR)
        val actionDelete : ImageView = itemView.findViewById(R.id.action_delete_PR)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.pending_request_card_list, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.println(Log.INFO,"debug","onBindViewHolder open" )
        val item = users?.get(position)
        Log.println(Log.INFO,"debug", "onBindViewHolder item : $item")
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.card.context, "Clicked: ${item?.username}", Toast.LENGTH_SHORT).show()
        }
        holder.actionValide.setOnClickListener {
            val showPopUp = FriendsPopup(item!!, FriendPopUpType.ACCEPT_PENDING, "Accepter ${item.username}", "Voulez vous vraiment accepter ${item.username} ?")
            showPopUp.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "Friends_popup")
        }
        holder.actionDelete.setOnClickListener {
            val showPopUp = FriendsPopup(item!!, FriendPopUpType.REFUSE_PENDING, "Rejeter ${item.username}", "Voulez vous vraiment rejeter la demande d'amis de ${item.username} ?")
            showPopUp.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "Friends_popup")
        }
        holder.textView.text = item?.displayname
        holder.descView.text = item?.username
        try {
            // Loading main gif
            Glide.with(holder.itemView.context)
                .load(item?.profilPicture)
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
        return users!!.size  // vraiment pas sur de ca
    }

}