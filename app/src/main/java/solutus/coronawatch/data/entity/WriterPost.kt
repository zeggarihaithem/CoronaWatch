package solutus.coronawatch.data.entity


import com.google.gson.annotations.SerializedName

data class WriterPost(
    val pk : String ,
    val content: String, // content
    @SerializedName("date_posted")
    val datePosted: String, // 2020-07-02 20:24
    val deleted: Boolean, // false
    val status: String, // pending
    val title: String, // title
    val user: Int // 5
)