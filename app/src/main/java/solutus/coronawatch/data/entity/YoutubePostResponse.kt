package solutus.coronawatch.data.entity

data class YoutubePostResponse(
    val next : Int? ,
    val previous: Int?,
    val results: List<VideoYoutube>?
)
