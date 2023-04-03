package solutus.coronawatch.data.cache.entity


import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("pk")
    val pk : Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("date_posted")
    val date_posted : String,
    @SerializedName("deleted")
    val deleted : Boolean,
    @SerializedName("status")
    val status : String,
    @SerializedName("content")
    val content: String,
    @SerializedName("user")
    val userId: Int,
    @SerializedName("file")
    val file : String


)