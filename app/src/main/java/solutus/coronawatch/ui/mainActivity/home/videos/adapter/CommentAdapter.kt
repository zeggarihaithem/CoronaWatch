package solutus.coronawatch.ui.mainActivity.home.videos.adapter

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch_mobile.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import solutus.coronawatch.data.entity.Comment
import solutus.coronawatch.data.entity.Reply
import solutus.coronawatch.data.internal.GetDataFromApiException
import solutus.coronawatch.data.network.implementation.ContentApi
import solutus.coronawatch.data.reposetory.implementation.ContentRepository
import solutus.coronawatch.ui.mainActivity.MainActivity

class CommentAdapter(val context: Context) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    private val contentRepository =
        ContentRepository(
            ContentApi()
        )
    private var comments: List<Comment> = ArrayList()
    private var listener: OnItemClickListener? = null
    private val isLogin = MainActivity.isLogin
    private lateinit var adapter: ReplyAdapter
    private lateinit var reply: String


    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): CommentHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: CommentHolder, position: Int) {
        val currentComment: Comment = comments[position]
        //set comment
        holder.commentView.text = currentComment.content
        //set user avatar
        if (currentComment.publisher.image != null) {
            Picasso.get().load(currentComment.publisher.image).into(holder.videoUserAvatar)
        }
        //set user name
        val userName = "${currentComment.publisher.firstName} ${currentComment.publisher.lastName}"
        holder.userNameView.text = userName
        //set comment date
        holder.dateView.text = currentComment.times
        //set Replies
        setReplies(position, holder.listReplies, holder.showRepliesButton)
        //watch reply edit text
        watchReply(holder.editReply, holder.sendReplyButton)

        //mode visitor
        isLogin.observe(context as LifecycleOwner, Observer {
            if (it) {
                if ((context as MainActivity).user.id == currentComment.publisher.id) {
                    holder.deleteButton.visibility = View.VISIBLE
                } else {
                    holder.deleteButton.visibility = View.GONE
                }
                holder.replyButton.visibility = View.VISIBLE
            } else {
                holder.deleteButton.visibility = View.GONE
                holder.replyButton.visibility = View.GONE
            }
        })

        holder.showRepliesButton.setOnClickListener {
            showReplies(holder.listReplies, holder.showRepliesButton, position)
        }
        holder.replyButton.setOnClickListener {
            replyToComment(holder.editReply, holder.sendReplyButton, holder.replyButton)
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun setComments(comments: List<Comment>) {
        this.comments = comments
        notifyDataSetChanged()
    }

    private fun replyToComment(
        editReply: EditText,
        sendReplyButton: ImageView,
        replyButton: TextView
    ) {
        editReply.visibility = if (editReply.visibility == View.VISIBLE) {
            sendReplyButton.visibility = View.GONE
            replyButton.text = "رد"
            View.GONE
        } else {
            replyButton.text = "الغاء"
            View.VISIBLE
        }
    }

    private fun showReplies(
        listReplies: RecyclerView,
        showRepliesButton: TextView,
        position: Int
    ) {
        listReplies.visibility = if (listReplies.visibility == View.VISIBLE) {
            val numberReplies = " ${comments[position].replies.size} ردود "
            showRepliesButton.text = numberReplies
            View.GONE
        } else {
            if (comments[position].replies.isNotEmpty()) showRepliesButton.text = "اخفاء"
            View.VISIBLE
        }
    }

    private fun setReplies(
        position: Int,
        listReplies: RecyclerView,
        showRepliesButton: TextView
    ) {
        val recyclerView: RecyclerView = listReplies
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        adapter = ReplyAdapter(context)
        recyclerView.adapter = adapter
        adapter.setReplies(comments[position].replies)
        //set replies number
        val numberReplies = " ${comments[position].replies.size} ردود "
        showRepliesButton.text = numberReplies
        adapter.setOnItemClickListener(object : ReplyAdapter.OnItemClickListener {
            override fun onItemClick(reply: Reply) {
                showDeleteDialog(reply)
            }
        })
    }

    private fun watchReply(
        editReply: EditText,
        sendReplyButton: ImageView
    ) {
        editReply.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.trim()?.isNotEmpty()!!) {
                    sendReplyButton.visibility = View.VISIBLE
                    reply = s.toString()
                } else {
                    sendReplyButton.visibility = View.GONE
                    editReply.isCursorVisible = false
                }
            }
        })
    }

    private fun showDeleteDialog(reply: Reply) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.delete_comment_dialog)
        val positiveButton = dialog.findViewById(R.id.positive_button) as Button
        val negativeButton = dialog.findViewById(R.id.negative_button) as Button
        positiveButton.setOnClickListener {
            deleteReply(reply)
            dialog.dismiss()
        }
        negativeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun deleteReply(reply: Reply) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                contentRepository.deleteComment((context as MainActivity).token, reply.id.toInt())
            } catch (e: GetDataFromApiException) {
                Log.d("Debug", "comment test   $e")
            } catch (e: Exception) {
                Log.d("Debug", "comment test   $e")
            }

        }
        Toast.makeText(context, "لقد تم حذف الرد", Toast.LENGTH_SHORT).show()
    }

    inner class CommentHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        internal val videoUserAvatar: de.hdodenhof.circleimageview.CircleImageView =
            itemView.findViewById(R.id.user_avatar)
        internal val userNameView: TextView = itemView.findViewById(R.id.user_name)
        internal val dateView: TextView = itemView.findViewById(R.id.date_view)
        internal val commentView: TextView = itemView.findViewById(R.id.comment_view)
        internal val showRepliesButton: TextView = itemView.findViewById(R.id.show_replies_button)
        internal val replyButton: TextView = itemView.findViewById(R.id.reply_button)
        internal val deleteButton: TextView = itemView.findViewById(R.id.delete_button)
        internal val editReply: EditText = itemView.findViewById(R.id.edit_reply)
        internal val sendReplyButton: ImageView = itemView.findViewById(R.id.send_reply_button)
        internal val listReplies: RecyclerView = itemView.findViewById(R.id.list_replies)



        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onDeleteClick(comments[position])
                }
            }
            sendReplyButton.setOnClickListener {
                editReply.text.clear()
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onSendReplyClick(comments[position], reply)
                }
            }
        }

    }

    interface OnItemClickListener {
        fun onDeleteClick(comment: Comment)
        fun onSendReplyClick(comment: Comment, reply: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

}
