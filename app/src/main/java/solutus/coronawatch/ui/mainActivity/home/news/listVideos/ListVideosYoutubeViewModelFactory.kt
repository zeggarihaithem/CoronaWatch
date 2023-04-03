package solutus.coronawatch.ui.mainActivity.home.news.listVideos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Job
import solutus.coronawatch.data.entity.VideoYoutube
import solutus.coronawatch.data.reposetory.implementation.VideosYoutubeRepository
import solutus.coronawatch.utilities.Coroutines

class ListVideosYoutubeViewModelFactory(private val videosYoutubeRepository: VideosYoutubeRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListVideosYoutubeViewModel(
            videosYoutubeRepository
        ) as T
    }

}