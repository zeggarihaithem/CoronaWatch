package solutus.coronawatch.data.network.implementation

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import solutus.coronawatch.data.entity.*
import solutus.coronawatch.data.network.abstraction.Api
import solutus.coronawatch.data.network.abstraction.SERVER_URL


private const val BASE_URL : String =  SERVER_URL +"api-content/"

interface ContentApi : Api {

    @GET("posts/")
    suspend fun getPosts(
    ) : Response<GetPostsResponse>

    @GET("posts/user/")
    suspend fun getUserPosts(
        @Header("Authorization") token :String
    ): Response<GetPostsResponse>

    @Multipart
    @POST("post/create/")
    suspend fun storePost(
        @Header("Authorization") token: String,
        @Part("title") title : String,
        @Part("content") content : String,
        @Part file : MultipartBody.Part
    )

    @PUT("post/delete/{id}")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body deleted: DeletePostRequest
    ): Response<Post>

    @GET("comment/")
    suspend fun getComments(
    ): Response<CommentResponse>

    @Multipart
    @POST("comment/create/")
    suspend fun storeComment(
        @Header("Authorization") token: String,
        @Part("post") post :Int,
        @Part("content") content : String,
        @Part("times") times : String
    )
    @Multipart
    @POST("comment/reply/create/")
    suspend fun storeReply(
        @Header("Authorization") token: String,
        @Part("post") post :Int,
        @Part("content") content : String,
        @Part("times") times : String,
        @Part("parent") parent : Int
    )

    @PUT("comment/delete/{id}")
    suspend fun deleteComment(
        @Header("Authorization") token :String,
        @Path ("id") id : Int,
        @Body deleted : DeleteCommentRequest
    ) : Response<ApiComment>



    @GET("writer-posts")
    suspend fun getWriterPosts(

    ): Response<WriterRequest>


    companion object {
        operator fun invoke() : ContentApi {

            return Api().baseUrl(BASE_URL)
                    .build()
                    .create(ContentApi::class.java)

        }
    }


}