package solutus.coronawatch.data.entity

data class InternationalZone (

    val id : Int,

    val x : Double,

    val y : Double,

    val name : String,

    val dead : Int,

    val sick : Int,

    val recovered : Int,

    val infected : Int,

    val continent : String?
)