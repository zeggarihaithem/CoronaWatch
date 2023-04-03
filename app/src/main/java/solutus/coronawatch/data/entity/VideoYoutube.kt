package solutus.coronawatch.data.entity

import com.google.gson.annotations.SerializedName


data class VideoYoutube(

    var title: String,
    @SerializedName("vedio")
    var video: String,
    var thumb: String

)



