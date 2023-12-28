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
import com.example.gifs_watcher.models.maps.models.GifMap


class LikesAdapter(val gifs: ArrayList<GifMap?>?, val callBack : (GifMap?)-> Unit) : RecyclerView.Adapter<LikesAdapter.ItemViewHolder>() {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.TitleGifA)
        val gif: ImageView = itemView.findViewById(R.id.GifA)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.profil_gif_card, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = gifs?.get(position)

        holder.title.text = item?.content_description
        Glide.with(holder.itemView.context)
            .load(item?.preview)
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