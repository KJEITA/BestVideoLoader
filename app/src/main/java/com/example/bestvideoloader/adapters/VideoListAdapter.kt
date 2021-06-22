package com.example.bestvideoloader.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.bestvideoloader.R

class VideoListAdapter(private val context: Context) :
    RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    var videos: ArrayList<Uri> = arrayListOf()

    fun setData(_videos: ArrayList<Uri>) {
        videos = _videos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       private val videoView =  itemView.findViewById <VideoView>(R.id.video)
        fun bind(uri: Uri) {
            videoView.setVideoURI(uri)
            videoView.requestFocus()
            videoView.start()
        }
    }
}