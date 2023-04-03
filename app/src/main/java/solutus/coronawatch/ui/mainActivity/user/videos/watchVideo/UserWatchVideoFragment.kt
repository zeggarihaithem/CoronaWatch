package solutus.coronawatch.ui.mainActivity.user.videos.watchVideo

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch_mobile.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.watch_video_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import solutus.coronawatch.data.entity.AppUser
import solutus.coronawatch.data.entity.Comment
import solutus.coronawatch.data.entity.Video
import solutus.coronawatch.data.network.implementation.ContentApi
import solutus.coronawatch.data.reposetory.implementation.ContentRepository
import solutus.coronawatch.ui.mainActivity.MainActivity
import solutus.coronawatch.ui.mainActivity.home.videos.adapter.CommentAdapter
import solutus.coronawatch.ui.mainActivity.home.videos.watchVideo.WatchVideoViewModel
import java.text.SimpleDateFormat
import java.util.*


class UserWatchVideoFragment : Fragment(), KodeinAware {


    override val kodein by closestKodein()
    private val viewModel: WatchVideoViewModel by instance()
    private lateinit var adapter: CommentAdapter
    private lateinit var mediaController: MediaController
    private lateinit var video: Video
    private lateinit var isLogin: MutableLiveData<Boolean>
    private lateinit var activity: MainActivity
    private val contentRepository =
        ContentRepository(
            ContentApi()
        )
    private lateinit var comment: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.watch_video_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isLogin = MainActivity.isLogin
        video = viewModel.video!!

        //set video
        setVideo()

        //set title
        title_text_view.text = video.title

        //set content
        content_text_view.text = video.content

        //set Comments
        setComments()

        //visitor cant comment
        isLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                divider1.visibility = View.VISIBLE
                user_avatar.visibility = View.VISIBLE
                edit_comment.visibility = View.VISIBLE
                if (getUserLogin().image != null) {
                    Picasso.get().load(getUserLogin().image).into(user_avatar)
                }
            } else {
                divider1.visibility = View.GONE
                user_avatar.visibility = View.GONE
                edit_comment.visibility = View.GONE
            }
        })


        //delete comment on button click
        adapter.setOnItemClickListener(object : CommentAdapter.OnItemClickListener {
            override fun onDeleteClick(comment: Comment) {
                showDeleteDialog(comment)
            }

            override fun onSendReplyClick(comment: Comment, reply: String) {
                sendReply(comment, reply)
            }
        })

        watchComment()

        send_button.setOnClickListener {
            sendComment()
        }
    }

    private fun showDeleteDialog(comment: Comment) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.delete_comment_dialog)
        val positiveButton = dialog.findViewById(R.id.positive_button) as Button
        val negativeButton = dialog.findViewById(R.id.negative_button) as Button
        positiveButton.setOnClickListener {
            deleteComment(comment)
            dialog.dismiss()
        }
        negativeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun sendReply(comment: Comment, reply: String) {
        viewModel.addReply(
            "Token ${activity.token}",
            video.id.toInt(),
            reply,
            getDate(),
            comment.id.toInt()
        )
        edit_comment.text.clear()
        Toast.makeText(activity, "لقد تم الرد بنجاح", Toast.LENGTH_SHORT).show()
        hideKeyBoard()

    }

    private fun sendComment() {
        viewModel.addComment(
            "Token ${activity.token}",
            video.id.toInt(),
            comment,
            getDate()
        )
        edit_comment.text.clear()
        Toast.makeText(activity, "لقد تم التعليق بنجاح", Toast.LENGTH_SHORT).show()
        hideKeyBoard()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    private fun setComments() {
        //set list comments
        val recyclerView: RecyclerView = list_comments as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter = CommentAdapter(requireActivity())
        recyclerView.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            val apiComments = contentRepository.getComment()
            if (apiComments != null) {
                viewModel.getComments(apiComments)
            }
        }

        viewModel.comments.observe(
            viewLifecycleOwner,
            Observer { comments ->
                val videoComments =
                    comments.filter { it.video == video.id.toInt() }
                adapter.setComments(videoComments)
                //set comments number
                comment_number.text = getNumberComment(videoComments).toString()
            })
    }

    private fun getNumberComment(comments: List<Comment>): Int {
        var nb = 0
        for (comment in comments) {
            nb += comment.replies.size
        }
        return nb + comments.size
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setVideo() {
        //set media controller
        mediaController = MediaController(context)
        video_view.setMediaController(mediaController)
        mediaController.setAnchorView(video_view)
        // set progress bar
        video_view.setOnPreparedListener { mp ->
            mp.start()
            mp.setOnVideoSizeChangedListener { mp, _, _ ->
                video_progress.visibility = View.GONE
                mp.start()
            }
        }
        video_view.setVideoURI(Uri.parse(video.url))
    }

    private fun watchComment() {
        edit_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.trim()?.isNotEmpty()!!) {
                    send_button.visibility = View.VISIBLE
                    comment = s.toString()
                } else {
                    send_button.visibility = View.GONE
                    edit_comment.isCursorVisible = false
                }
            }
        })
    }

    private fun deleteComment(comment: Comment) {
        viewModel.deleteComment(activity.token, comment.id.toInt())
        Toast.makeText(activity, "لقد تم حذف التعليق", Toast.LENGTH_SHORT).show()
    }

    private fun getUserLogin(): AppUser {
        return activity.user
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {
        val sdf = SimpleDateFormat("yyyy/M/dd hh:mm")
        return sdf.format(Date())
    }

    private fun hideKeyBoard() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}
