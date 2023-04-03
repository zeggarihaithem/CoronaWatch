package solutus.coronawatch.ui.mainActivity.home.videos.watchVideo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import solutus.coronawatch.data.entity.ApiComment
import solutus.coronawatch.data.entity.Comment
import solutus.coronawatch.data.entity.Video
import solutus.coronawatch.data.internal.GetDataFromApiException
import solutus.coronawatch.data.reposetory.implementation.ContentRepository
import solutus.coronawatch.utilities.Coroutines.ioThMain

class WatchVideoViewModel(
    private val contentRepository: ContentRepository
) : ViewModel() {
    var video: Video? = null
    private lateinit var job: Job
    private val _comments = MutableLiveData<List<Comment>>()

    val comments: LiveData<List<Comment>>
        get() = _comments


    fun getComments(apiComments: ArrayList<ApiComment>) {
        job = ioThMain(
            { contentRepository.createComments(apiComments) },
            { _comments.value = it }
        )
    }

    fun addComment(token: String, post: Int, content: String, times: String) {
        CoroutineScope(IO).launch {
            try {
                contentRepository.postComment(token, post, content, times)
            } catch (e: GetDataFromApiException) {
                Log.d("Debug", "comment test   $e")
            } catch (e: Exception) {
                Log.d("Debug", "comment test   $e")
            }
        }
    }

    fun addReply(token: String, post: Int, content: String, times: String, parent: Int) {
        CoroutineScope(IO).launch {
            try {
                contentRepository.postReply(token, post, content, times, parent)
            } catch (e: GetDataFromApiException) {
                Log.d("Debug", "comment test   ${e.toString()}")
            } catch (e: Exception) {
                Log.d("Debug", "comment test   ${e.toString()}")
            }

        }
    }

    fun deleteComment(token: String, id: Int) {
        CoroutineScope(IO).launch {
            try {
                contentRepository.deleteComment(token, id)
            } catch (e: GetDataFromApiException) {
                Log.d("Debug", "comment test   ${e.toString()}")
            } catch (e: Exception) {
                Log.d("Debug", "comment test   ${e.toString()}")
            }

        }
    }

    override fun onCleared() {
        if (::job.isInitialized) {
            job.cancel()
        }
    }
}