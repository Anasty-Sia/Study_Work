package com.example.study_work

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class TrackViewHorder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val artworkImageView: ImageView
    private val trackNameView: TextView
    private val artistNameView: TextView
    private val trackTimeView: TextView



    init {
        artworkImageView = itemView.findViewById(R.id.artworkUrlImage)
        trackNameView = itemView.findViewById(R.id.trackName)
        artistNameView = itemView.findViewById(R.id.artistName)
        trackTimeView = itemView.findViewById(R.id.trackTime)
    }


    fun bind(track: Track) {
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text = track.getFormattedTime()

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
            .into(artworkImageView)
    }
}
