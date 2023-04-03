package solutus.coronawatch.ui.mainActivity.user.videos.listVideos

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.user_videos_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import solutus.coronawatch.data.entity.Post
import solutus.coronawatch.data.entity.Video
import solutus.coronawatch.data.network.implementation.ContentApi
import solutus.coronawatch.data.reposetory.implementation.ContentRepository
import solutus.coronawatch.ui.mainActivity.MainActivity
import solutus.coronawatch.ui.mainActivity.home.videos.listVideos.VideoViewModelFactory
import solutus.coronawatch.ui.mainActivity.home.videos.listVideos.VideosViewModel
import solutus.coronawatch.ui.mainActivity.home.videos.watchVideo.WatchVideoViewModel
import solutus.coronawatch.ui.mainActivity.user.videos.adapter.UserVideoAdapter
import solutus.coronawatch.utilities.InjectorUtils


class UserVideosFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var activity: MainActivity
    private lateinit var viewModelFactory: VideoViewModelFactory
    private lateinit var viewModel: VideosViewModel
    private val contentRepository =
        ContentRepository(
            ContentApi.invoke()
        )
    private lateinit var posts: ArrayList<Post>
    private lateinit var status: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_videos_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {
        //set recycle view adapter
        val recyclerView: RecyclerView = list_video as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        val adapter =
            UserVideoAdapter(
                activity
            )
        recyclerView.adapter = adapter

        //set ViewModel
        viewModelFactory = (InjectorUtils.provideVideosViewModelFactory())
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideosViewModel::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val posts = activity.token.let { contentRepository.getUserPosts(it) }
            if (posts != null) {

                activity.user.let { viewModel.getUserVideos(posts, it) }
            }
        }

        viewModel.userVideos.observe(
            viewLifecycleOwner,
            Observer { videos -> adapter.setVideos(videos as List<Video>) })
        //go to view Video on thumbnail click
        adapter.setOnItemClickListener(object : UserVideoAdapter.OnItemClickListener {
            override fun onItemClick(video: Video) {
                watchVideo(video)
                status = view!!.findViewById(R.id.status_text)
            }
        })
        adapter.setOnItemLongClickListener(object : UserVideoAdapter.OnItemLongClickListener {
            override fun onItemLongClick(video: Video): Boolean {
                showDialog(activity.token, (video.id).toInt())
                return true
            }
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    private fun watchVideo(video: Video) {
        //go to ViewVideoFragment
        val viewModel: WatchVideoViewModel by instance()
        viewModel.video = video
        val navController: NavController =
            Navigation.findNavController(requireActivity(), R.id.user_nav_host_fragment)
        navController.navigate(R.id.to_watch_video_fragment_action)
    }

    private fun showDialog(token: String, id: Int) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.costume_dialog_update_delete)

        val Btn = dialog.findViewById(R.id.deleteBtn) as Button

        Btn.setOnClickListener {
            CoroutineScope(IO).launch {
                        contentRepository.deletePost(token, id)
                        dialog.dismiss()
                    }
                }
                dialog.show()

            }

        }

