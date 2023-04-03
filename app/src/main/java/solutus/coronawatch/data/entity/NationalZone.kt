package solutus.coronawatch.data.entity

import com.google.gson.annotations.SerializedName

data class NationalZone (

    val id : Int,

    val x : Double,

    val y : Double,

    val name : String,

    val dead : Int,

    val sick : Int,

    val recovered : Int,

    val infected : Int,

    val is_risky : Boolean,

    val remarque : String?,

    val centre : Boolean
)