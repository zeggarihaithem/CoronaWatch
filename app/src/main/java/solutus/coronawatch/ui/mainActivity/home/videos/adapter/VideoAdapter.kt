package solutus.coronawatch.ui.mainActivity.home.videos.adapter

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
import solutus.coronawatch.data.entity.Video


class VideoAdapter(val context: Context) : RecyclerView.Adapter<VideoAdapter.VideoHolder>() {
    private var videos: List<Video> = ArrayList()
    private var listener: OnItemClickListener? = null
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): VideoHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item, parent, false)
        return VideoHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: VideoHolder, position: Int) {
        val currentVideo: Video = videos[position]
        //set video title
        holder.videoTitleView.text = currentVideo.title
        //set video thumbnil
        Picasso.get().load(currentVideo.thumbnail).into(holder.videoThumbnail)
        //set user name
        val userName = currentVideo.publisher.firstName + " " + currentVideo.publisher.lastName
        holder.videoUserName.text = userName
        //set user avatar
        Picasso.get().load(currentVideo.publisher.image).into(holder.videoUserAvatar)

    }

    override fun getItemCount(): Int {
        return videos.size
    }

    fun setVideos(videos: List<Video>) {
        this.videos = videos
        notifyDataSetChanged()
    }


    inner class VideoHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        internal val videoTitleView: TextView = itemView.findViewById(R.id.video_title)
        internal val videoThumbnail: ImageView = itemView.findViewById(R.id.video_thumbnil)
        internal val videoUserName: TextView = itemView.findViewById(R.id.user_name)
        internal val videoUserAvatar: de.hdodenhof.circleimageview.CircleImageView =
            itemView.findViewById(R.id.user_avatar)

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
        fun onItemClick(video: Video)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

}
