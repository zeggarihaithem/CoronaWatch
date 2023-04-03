package solutus.coronawatch.data.network.entity

import okhttp3.MultipartBody

data  class UploadedVideo(
    val title : String ,
    val video: MultipartBody.Part  ,
    val content : String
)