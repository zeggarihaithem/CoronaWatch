package solutus.coronawatch.data.entity

data class NationalZoneInfos(
    val name : String ,

    val dead : Int,

    val sick : Int,

    val recovered : Int,

    val infected : Int,

    val is_risky : Boolean,

    val remarque : String?,

    val centre : Boolean
)