package solutus.coronawatch.data.entity

data class WriterRequest(
    val next : Int? ,
    val previous: Int?,
    val results: List<WriterPost>?
)