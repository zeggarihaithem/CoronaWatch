package solutus.coronawatch.data.reposetory.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import solutus.coronawatch.data.entity.VideoYoutube
import solutus.coronawatch.data.network.implementation.ContentApi
import solutus.coronawatch.data.network.implementation.RobotApi
import solutus.coronawatch.data.reposetory.abstraction.SafeApiRequest
import solutus.coronawatch.utilities.Coroutines

class VideosYoutubeRepository(
    private val api: RobotApi
) : SafeApiRequest() {

    private val  _youtubeVideos  = MutableLiveData<List<VideoYoutube>>()

     val youtubeVideos : LiveData<List<VideoYoutube>>
        get () = _youtubeVideos

     //pour tester
    suspend  fun getVideosYoutube() {

       val youtubeList =   apiRequest { api.getRobotVideos() }.results
       CoroutineScope(Main).launch {
          _youtubeVideos.postValue( youtubeList)
      }
    }
}