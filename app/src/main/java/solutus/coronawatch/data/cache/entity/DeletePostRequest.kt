package solutus.coronawatch.data.cache.entity

import com.google.gson.annotations.SerializedName

data class DeletePostRequest(
    @SerializedName("deleted")
    val deleted : String
)