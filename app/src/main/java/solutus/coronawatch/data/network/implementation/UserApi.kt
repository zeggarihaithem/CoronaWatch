package solutus.coronawatch.data.network.implementation

import retrofit2.Response
import retrofit2.http.*
import solutus.coronawatch.data.entity.AppUser
import solutus.coronawatch.data.entity.RegisterPostRequest
import solutus.coronawatch.data.network.abstraction.Api
import solutus.coronawatch.data.network.abstraction.SERVER_URL
import solutus.coronawatch.data.network.entity.Token


private const val BASE_URL: String = SERVER_URL + "api_account/"

interface UserApi : Api {


    @GET("current-user")
    suspend fun getAuthUser(
        @Header("Authorization") token: String
    ) : Response<AppUser>

    @GET("user/{id}")
    suspend fun getUser(
        @Path("id") id : Int
    ): Response<AppUser>


    @POST("register/")
    suspend fun addUser(
        @Body registerPostRequest : RegisterPostRequest
    ) : Response<AppUser>

    @POST("login/visitor/")
    suspend fun loginUser(
        @Body emailPassword : HashMap<String , String>
    ) : Response<Token>


    @POST("social/facebook/")
    suspend fun loginWithFacebook(
        @Body accessToken: HashMap<String,String>
    ):Response<Token>

    companion object {
        operator fun invoke() : UserApi {

            return Api().baseUrl(BASE_URL)
                .build()
                .create(UserApi::class.java)

        }
    }
}