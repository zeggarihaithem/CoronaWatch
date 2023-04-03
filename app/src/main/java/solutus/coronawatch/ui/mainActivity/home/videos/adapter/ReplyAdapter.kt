package solutus.coronawatch.ui.mainActivity.home.videos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch_mobile.R
import com.squareup.picasso.Picasso
import solutus.coronawatch.data.entity.Reply
import solutus.coronawatch.ui.mainActivity.MainActivity

class ReplyAdapter(val context: Context) : RecyclerView.Adapter<ReplyAdapter.ReplyHolder>() {

    private var replies: List<Reply> = ArrayList()
    private var listener: OnItemClickListener? = null
    private val isLogin = MainActivity.isLogin
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ReplyHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.reply_item, parent, false)
        return ReplyHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: ReplyHolder, position: Int) {
        val currentReply: Reply = replies[position]
        //set reply
        holder.commentView.text = currentReply.content
        //set user avatar
        if (currentReply.publisher.image != null) {
            Picasso.get().load(currentReply.publisher.image).into(holder.videoUserAvatar)
        }
        //set user name
        val userName = "${currentReply.publisher.firstName} ${currentReply.publisher.lastName}"
        holder.userNameView.text = userName
        //set comment date
        holder.dateView.text = currentReply.times

        //mode visitor
        isLogin.observe(context as LifecycleOwner, Observer {
            if (it) {
                if ((context as MainActivity).user.id == currentReply.publisher.id) {
                    holder.deleteButton.visibility = View.VISIBLE
                } else {
                    holder.deleteButton.visibility = View.GONE
                }
            } else {
                holder.deleteButton.visibility = View.GONE
            }
        })

    }

    override fun getItemCount(): Int {
        return replies.size
    }

    fun setReplies(replies: List<Reply>) {
        this.replies = replies
        notifyDataSetChanged()
    }


    inner class ReplyHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        internal val videoUserAvatar: de.hdodenhof.circleimageview.CircleImageView =
            itemView.findViewById(R.id.user_avatar)
        internal val userNameView: TextView = itemView.findViewById(R.id.user_name)
        internal val commentView: TextView = itemView.findViewById(R.id.comment_view)
        internal val deleteButton: TextView = itemView.findViewById(R.id.delete_button)
        internal val dateView: TextView = itemView.findViewById(R.id.date_view)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(replies[position])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(reply: Reply)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

}