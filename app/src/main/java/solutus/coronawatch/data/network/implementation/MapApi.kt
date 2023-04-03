package solutus.coronawatch.data.network.implementation

import retrofit2.Response
import retrofit2.http.GET
import solutus.coronawatch.data.entity.GetInterNationalZoneResponse
import solutus.coronawatch.data.entity.GetNationalZoneResponse
import solutus.coronawatch.data.network.abstraction.Api
import solutus.coronawatch.data.network.abstraction.SERVER_URL

private const val BASE_URL : String =  SERVER_URL +"api-map/"

interface MapApi : Api {

    @GET("national-zone")
    suspend fun getNationalZones() : Response<GetNationalZoneResponse>

    @GET("international-zone")
    suspend fun getInterNationalZones() : Response<GetInterNationalZoneResponse>

    companion object {
        operator fun invoke() : MapApi {

            return Api().baseUrl(BASE_URL)
                .build()
                .create(MapApi::class.java)

        }
    }
}