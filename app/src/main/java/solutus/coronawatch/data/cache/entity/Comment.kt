package solutus.coronawatch.data.cache.entity
data class Comment (
    val  user : String,
    val post : Post,
    var text : String?
)