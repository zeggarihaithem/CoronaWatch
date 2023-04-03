package solutus.coronawatch.data.network.implementation

import retrofit2.Response
import retrofit2.http.GET
import solutus.coronawatch.data.entity.YoutubePostResponse
import solutus.coronawatch.data.network.abstraction.Api
import solutus.coronawatch.data.network.abstraction.SERVER_URL

private const val BASE_URL : String =  SERVER_URL +"api-robot/"
interface RobotApi : Api {



    @GET("youtube-vedios-list/")
    suspend fun getRobotVideos(

    ) : Response<YoutubePostResponse>


    companion object {
        operator fun invoke() : RobotApi {

            return Api().baseUrl(BASE_URL)
                .build()
                .create(RobotApi::class.java)

        }
    }


}