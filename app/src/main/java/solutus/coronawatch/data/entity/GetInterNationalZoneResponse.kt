package solutus.coronawatch.data.entity

import com.google.gson.annotations.SerializedName

data class GetInterNationalZoneResponse (
    @SerializedName("count")
    val count : Int,
    @SerializedName("next")
    val next : String,
    @SerializedName("previous")
    val previous : String,
    @SerializedName("results")
    val results : ArrayList<InternationalZone>
)