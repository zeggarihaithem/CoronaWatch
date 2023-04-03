package solutus.coronawatch.data.reposetory.implementation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import solutus.coronawatch.data.entity.Article
import solutus.coronawatch.data.entity.WriterPost
import solutus.coronawatch.data.network.implementation.ContentApi
import solutus.coronawatch.data.network.implementation.RobotApi
import solutus.coronawatch.data.reposetory.abstraction.SafeApiRequest
import java.lang.Exception

class ArticlesRepository(
    private val contentApi: ContentApi ,
    private val userRepository : UserRepository
) : SafeApiRequest() {

    private val _articles = MutableLiveData<List<Article>>()

    val articles :LiveData<List<Article>>
        get()  =_articles

    //pour tester
    fun getArticles(){
        CoroutineScope(IO).launch {
           try {
               Log.d("Debug","This is the list of Post ")
               val articlesResult = ArrayList<Article>()
               val writerPostList : List<WriterPost>? = getWriterPost()
               println("Debug : ${writerPostList?.size}")
               for ( writerPost in writerPostList!!) {
                //get the image en parallel
                    val appUser =    userRepository.getUser(writerPost.user)
                     articlesResult.add(
                        Article(
                            publisher = appUser,
                            url = writerPost.content,
                            title = writerPost.title,
                            id = writerPost.pk
                        )
                    )
                    println("Debug this is my writerPost$writerPost")
                }
               CoroutineScope(Main).launch {
                   _articles.postValue(articlesResult)
               }
           } catch (e : Exception){

           }
        }
    }

     private suspend fun getWriterPost() : List<WriterPost>?{
        Log.d("Debug " , "Get the data from APi")
        return apiRequest {
            contentApi.getWriterPosts()
        }.results
    }
}