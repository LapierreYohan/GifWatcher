package com.example.gifs_watcher.utils.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.gifs_watcher.R
import com.example.gifs_watcher.models.User


class SentRequesteAdapter(val users: ArrayList<User?>?) : RecyclerView.Adapter<SentRequesteAdapter.ItemViewHolder>() {

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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.println(Log.INFO,"debug","onBindViewHolder open" )
        val item = users?.get(position)
        Log.println(Log.INFO,"debug","onBindViewHolder item : " + item )
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.card.context, "Clicked: ${item?.username}", Toast.LENGTH_SHORT).show()
        }
        holder.action.setOnClickListener {
            Toast.makeText(holder.card.context, "Clicked: delete ${item?.username}", Toast.LENGTH_SHORT).show()
        }
        holder.textView.text = item?.username
        holder.descView.text = item?.bio
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