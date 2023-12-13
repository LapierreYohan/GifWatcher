package com.example.gifs_watcher.views.main.profil.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.gifs_watcher.R
import com.example.gifs_watcher.models.Results


class LikesAdapter(val gifs: ArrayList<Results?>?, val callBack : (Results?)-> Unit) : RecyclerView.Adapter<LikesAdapter.ItemViewHolder>() {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.TitleGifA)
        val gif: ImageView = itemView.findViewById(R.id.GifA)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.gif_card, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = gifs?.get(position) // met dans item l'utilisateur

        //set les valeurs de l'utilsateur dans les différents champs
        holder.title.text = item?.contentDescription
        Glide.with(holder.itemView.context)
            .load(item?.media?.get(0)?.gif?.url)
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(20)
                    )
                )
            )
            .into(holder.gif)
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return gifs!!.size  // vraiment pas sur de ca
    }

}