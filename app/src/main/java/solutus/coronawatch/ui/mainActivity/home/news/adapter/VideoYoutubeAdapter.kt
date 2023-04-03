package solutus.coronawatch.ui.mainActivity.home.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch_mobile.R
import com.squareup.picasso.Picasso
import solutus.coronawatch.data.entity.VideoYoutube

class VideoYoutubeAdapter(val context: Context) :
    RecyclerView.Adapter<VideoYoutubeAdapter.VideoYoutubeHolder>() {
    private var videos: List<VideoYoutube> = ArrayList()
    private var listener: OnItemClickListener? = null
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): VideoYoutubeHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_youtube_item, parent, false)
        return VideoYoutubeHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: VideoYoutubeHolder, position: Int) {
        val currentVideo: VideoYoutube = videos[position]
        //set video title
        holder.videoTitleView.text = currentVideo.title
        //set video thumbnil
        Picasso.get().load(currentVideo.thumb).into(holder.videoThumbnail)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    fun setVideos(videos: List<VideoYoutube>) {
        this.videos = videos
        notifyDataSetChanged()
    }


    inner class VideoYoutubeHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        internal val videoTitleView: TextView = itemView.findViewById(R.id.video_title)
        internal val videoThumbnail: ImageView = itemView.findViewById(R.id.video_thumbnil)


        init {
            videoThumbnail.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(videos[position])
                }
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(video: VideoYoutube)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

}