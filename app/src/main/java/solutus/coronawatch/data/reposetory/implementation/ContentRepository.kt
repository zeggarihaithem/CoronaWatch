package solutus.coronawatch.data.reposetory.implementation

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import solutus.coronawatch.data.entity.*
import solutus.coronawatch.data.network.implementation.ContentApi
import solutus.coronawatch.data.network.implementation.UserApi
import solutus.coronawatch.data.reposetory.abstraction.SafeApiRequest
import solutus.coronawatch.utilities.RealPathUtil
import java.io.File


class ContentRepository (
    private val contentApi: ContentApi
): SafeApiRequest() {



    suspend fun postVideo(token : String , title: String ,content: String,video : MultipartBody.Part  ) {
        contentApi.storePost(token, title, content, video)
    }

    suspend fun postVideo(token : String , title: String ,content: String,video : Uri ,context : Context  ) {

        val realPath :String? = RealPathUtil.getRealPath(context,video)
        val originalFile : File = File(realPath!!)
        val str = context.contentResolver?.getType(video) as String
        val file : RequestBody = RequestBody.create(str.toMediaTypeOrNull(),originalFile)

        val videoP : MultipartBody.Part = MultipartBody.Part.createFormData("file",originalFile.name,file )

        contentApi.storePost(token,title,content,videoP)

    }

    suspend fun getPosts() : ArrayList<Post>? {
        return contentApi.getPosts().body()!!.posts.filter { (it.status == "accepted") && !it.deleted } as ArrayList<Post>?
    }

    suspend fun getUserPosts(token: String) : ArrayList<Post>?{
        return contentApi.getUserPosts("token $token").body()!!.posts
    }

    suspend fun deletePost(token: String,id:Int){
        val deleted = DeletePostRequest("true")
        contentApi.deletePost("token $token",id,deleted)
    }

    suspend fun createVideos(posts : ArrayList<Post>) : ArrayList<Video>{


        val userRepository =
            UserRepository(
                UserApi.invoke()
            )
        lateinit var user : AppUser
        val listv = ArrayList<Video>()

        for (post in posts) {
            user = userRepository.getUser(post.userId)
            listv.add(
                Video(
                    id = post.pk.toString(),
                    publisher = user,
                    title = post.title,
                    url = post.file,
                    content = post.content,
                    thumbnail = "https://www.gynecologie-pratique.com/sites/www.gynecologie-pratique.com/files/images/article_journal/covid-19.png"
                )
            )

        }

        return listv
    }

    fun  createUserVideos(posts : ArrayList<Post> , user: AppUser) : ArrayList<Video>{
        val listv = ArrayList<Video>()
        for (post in posts ){
            listv.add(
                Video(
                    id = post.pk.toString(),
                    publisher = user,
                    title = post.title,
                    url = post.file,
                    content = post.content,
                    thumbnail = "https://www.gynecologie-pratique.com/sites/www.gynecologie-pratique.com/files/images/article_journal/covid-19.png"
                )
            )
        }
        return listv
    }


    ///Comments
    suspend fun getComment(): ArrayList<ApiComment>? {
        return contentApi.getComments().body()!!.comments.filter { (!it.deleted) }.map { it ->
            it.copy(replies = it.replies.filter { !it.deleted })
        } as ArrayList<ApiComment>?
    }

    suspend fun createComments(apiComments: ArrayList<ApiComment>): ArrayList<Comment> {
        val userRepository =
            UserRepository(
                UserApi.invoke()
            )
        lateinit var user: AppUser
        val listComments = ArrayList<Comment>()

        for (apiComment in apiComments) {
            user = userRepository.getUser(apiComment.publisher)
            listComments.add(
                Comment(
                    id = apiComment.id.toString(),
                    publisher = user,
                    video = apiComment.post,
                    content = apiComment.content,
                    times = apiComment.times,
                    replies = getReplies(apiComment.replies)
                )
            )

        }
        return listComments
    }

    private suspend fun getReplies(apiReplies: List<ApiReply>): ArrayList<Reply> {
        val userRepository =
            UserRepository(
                UserApi.invoke()
            )
        lateinit var user: AppUser
        val listReplies = ArrayList<Reply>()

        for (apiComment in apiReplies) {
            user = userRepository.getUser(apiComment.publisher)
            listReplies.add(
                Reply(
                    id = apiComment.id.toString(),
                    publisher = user,
                    video = apiComment.post,
                    content = apiComment.content,
                    times = apiComment.times,
                    parent = apiComment.parent
                )
            )

        }
        return listReplies
    }

    suspend fun postComment(token: String, post: Int, content: String, times: String) {
        contentApi.storeComment(token, post, content, times)
    }

    suspend fun postReply(token: String, post: Int, content: String, times: String, parent: Int) {
        contentApi.storeReply(token, post, content, times, parent)
    }

    suspend fun deleteComment(token: String, id: Int) {
        val deleted = DeleteCommentRequest("true")
        contentApi.deleteComment("token $token", id, deleted)
    }



}