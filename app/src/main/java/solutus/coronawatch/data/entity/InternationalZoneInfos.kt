package solutus.coronawatch.data.entity

data class InternationalZoneInfos (
    val name : String ,

    val dead : Int,

    val sick : Int,

    val recovered : Int,

    val infected : Int,

    val continent : String?
)