package com.example.study_work

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
private var  tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHorder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHorder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHorder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHorder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }


    fun updateTracks(newTrack: List<Track>) {
        tracks = newTrack
        notifyDataSetChanged()
    }
}