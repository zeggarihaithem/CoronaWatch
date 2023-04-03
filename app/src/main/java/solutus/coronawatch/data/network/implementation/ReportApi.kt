package solutus.coronawatch.data.network.implementation

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import solutus.coronawatch.data.entity.Location
import solutus.coronawatch.data.network.abstraction.Api
import solutus.coronawatch.data.network.abstraction.SERVER_URL
import solutus.coronawatch.data.network.entity.Token
import java.io.File


private const val BASE_URL: String = SERVER_URL +"api-report/"
interface ReportApi  : Api{


    @Multipart
    @POST("case/report/")
    suspend fun reportCase(
        @Header("Authorization") token: String,
        //@Body caseToReport: MultipartBody
        @Part("description") description : RequestBody ,
        @Part("x") x : Float ,
        @Part("y") y : Float ,
        @Part attachment : MultipartBody.Part
    ) : Response<Unit>

    companion object {
        operator fun invoke() : ReportApi {
            return Api().baseUrl(BASE_URL)
                .build()
                .create(ReportApi::class.java)

        }
    }
}