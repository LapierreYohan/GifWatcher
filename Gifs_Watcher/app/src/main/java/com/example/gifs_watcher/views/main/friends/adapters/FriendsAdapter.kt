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
import com.example.gifs_watcher.utils.enums.Friend_PopUp_type
import com.example.gifs_watcher.views.main.popUp.Friends_popup


class FriendsAdapter(val users: ArrayList<User?>?, val callBack : (User?)-> Unit) : RecyclerView.Adapter<FriendsAdapter.ItemViewHolder>() {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_title)
        val descView: TextView = itemView.findViewById(R.id.tv_subTitle)
        val card: ConstraintLayout = itemView.findViewById(R.id.friend_card)
        val gif: ImageView = itemView.findViewById(R.id.Iv_preview)
        val action : ImageView = itemView.findViewById(R.id.action_delete_F)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.friends_card_list, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = users?.get(position) // met dans item l'utilisateur

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.card.context, "Clicked: ${item?.username}", Toast.LENGTH_SHORT).show()
            callBack(item)
        }
        holder.action.setOnClickListener {
            val showPopUp = Friends_popup(item!!, Friend_PopUp_type.delete_friend, "Supprimer", "Voulez vous vraiment supprimer ${item?.username} ?")

            showPopUp.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "Friends_popup")
        }

        //set les valeurs de l'utilsateur dans les différents champs
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