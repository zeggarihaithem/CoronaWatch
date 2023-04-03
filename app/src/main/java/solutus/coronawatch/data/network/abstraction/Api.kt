package solutus.coronawatch.data.network.abstraction

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import solutus.coronawatch.data.network.implementation.ContentApi
import java.util.concurrent.TimeUnit


const val SERVER_URL = "http://solutusprojet.herokuapp.com/"

interface Api {

    companion object {
        operator fun invoke() : Retrofit.Builder {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .addInterceptor(logging)


            return  Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())


        }
    }

}