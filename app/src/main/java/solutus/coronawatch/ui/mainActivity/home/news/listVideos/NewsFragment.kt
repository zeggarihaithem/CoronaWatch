package solutus.coronawatch.ui.mainActivity.home.news.listVideos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.news_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import solutus.coronawatch.data.entity.VideoYoutube
import solutus.coronawatch.ui.mainActivity.home.news.adapter.VideoYoutubeAdapter
import solutus.coronawatch.utilities.InjectorUtils

class NewsFragment : Fragment() , KodeinAware {


    override val kodein by closestKodein()

    private  val viewModel: ListVideosYoutubeViewModel by instance<ListVideosYoutubeViewModel>()

    private lateinit var adapter: VideoYoutubeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initializeUi()
        ////go to watch Video on thumbnail click
        adapter.setOnItemClickListener(object : VideoYoutubeAdapter.OnItemClickListener {
            override fun onItemClick(video: VideoYoutube) {
                watchVideo(video)
            }
        })
    }

    private fun watchVideo(video: VideoYoutube) {
        //pass data to watch video fragment using bundle
        val bundle = Bundle()
             bundle.putString("url", video.video)
        Log.d("Debug " ,video.video )
        //go to ViewVideoFragment
        val navController: NavController =
            Navigation.findNavController(requireActivity(), R.id.nav_home_fragment)
        navController.navigate(R.id.to_watch_video_youtube_fragment_action, bundle)
    }

    private fun initializeUi() {
        //set recycle view adapter
        val recyclerView: RecyclerView = list_video_youtube as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter = VideoYoutubeAdapter(requireActivity())
        recyclerView.adapter = adapter

        viewModel.getVideosYoutube()
        viewModel.videosYoutube.observe(viewLifecycleOwner, Observer { videos ->

               if(videos.isEmpty()){
                   youtube_progressBar.visibility = View.VISIBLE
               }else{
                   youtube_progressBar.visibility = View.GONE
               }
                adapter.setVideos(videos as List<VideoYoutube>)
        })
    }

}


