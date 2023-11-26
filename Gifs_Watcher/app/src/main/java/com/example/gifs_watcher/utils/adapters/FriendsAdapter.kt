package com.example.gifs_watcher.utils.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.gifs_watcher.R
import com.example.gifs_watcher.models.User


class FriendsAdapter(val users: ArrayList<User?>?, val callBack : (User?)-> Unit) : RecyclerView.Adapter<FriendsAdapter.ItemViewHolder>() {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_title)
        val descView: TextView = itemView.findViewById(R.id.tv_subTitle)
        val card: ConstraintLayout = itemView.findViewById(R.id.friend_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.pending_request, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = users?.get(position) // met dans item l'utilisateur

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.card.context, "Clicked: ${item?.username}", Toast.LENGTH_SHORT).show()
            callBack(item)
        }

        //set les valeurs de l'utilsateur dans les différents champs
        holder.textView.text = item?.username
        holder.descView.text = item?.bio
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return users!!.size  // vraiment pas sur de ca
    }

}