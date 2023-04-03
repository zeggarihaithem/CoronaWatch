package solutus.coronawatch.ui.mainActivity.home.videos.listVideos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import solutus.coronawatch.data.entity.AppUser
import solutus.coronawatch.data.entity.Post
import solutus.coronawatch.data.entity.Video
import solutus.coronawatch.data.reposetory.implementation.ContentRepository
import solutus.coronawatch.utilities.Coroutines


open class VideosViewModel(
    private val contentRepository : ContentRepository
) : ViewModel() {

    private lateinit var job : Job
    private val _videos = MutableLiveData<List<Video>>()
    private val _userVideos = MutableLiveData<List<Video>>()


    val videos : LiveData<List<Video>>
        get() = _videos


    fun getVideos(posts: ArrayList<Post>) {

        job = Coroutines.ioThMain(
            {contentRepository.createVideos(posts)},
            {_videos.value = it}
        )
    }

    fun getUserVideos(posts: ArrayList<Post>,user: AppUser) {

        job = Coroutines.ioThMain(
            {contentRepository.createUserVideos(posts,user)},
            {_userVideos.value = it}
        )
    }


    val userVideos : LiveData<List<Video>>
            get() = _userVideos


    override fun onCleared() {
        if(::job.isInitialized){
            job.cancel()
        }
    }
}
