package solutus.coronawatch.data.entity

import com.google.gson.annotations.SerializedName

class ApiReply(
    @SerializedName("pk") val id: Int,
    @SerializedName("user") val publisher: Int,
    @SerializedName("post") val post: Int,
    @SerializedName("content") var content: String,
    @SerializedName("times") val times: String,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("parent") val parent: Int


)