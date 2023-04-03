package solutus.coronawatch.ui.mainActivity.home.videos.listVideos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import solutus.coronawatch.data.reposetory.implementation.ContentRepository


/**
 *  This class is for passing  the VideosRepository to the VideoFragment
 *  when we create the instance of the VideoViewModel
 */

open class VideoViewModelFactory(
    private val contentRepository: ContentRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VideosViewModel(
            contentRepository
        ) as T
    }

}