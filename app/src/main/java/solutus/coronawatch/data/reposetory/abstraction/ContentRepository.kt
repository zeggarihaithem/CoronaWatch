package solutus.coronawatch.data.reposetory.abstraction


import solutus.coronawatch.data.entity.AppUser
import solutus.coronawatch.data.entity.Post
import solutus.coronawatch.data.entity.Video
import solutus.coronawatch.data.network.entity.UploadedVideo

interface ContentRepository {


    suspend fun getPosts() : ArrayList<Post>?

    suspend fun postVideo(video : UploadedVideo)

    suspend fun getUserPosts(token: String) : ArrayList<Post>?

    suspend fun deletePost(token: String,id:Int)

    suspend fun createVideos(posts : ArrayList<Post>) : ArrayList<Video>

    fun  createUserVideos(posts : ArrayList<Post> , user: AppUser) : ArrayList<Video>

}