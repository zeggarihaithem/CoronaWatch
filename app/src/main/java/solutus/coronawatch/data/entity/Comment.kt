package solutus.coronawatch.data.entity

data class Comment(
    override val id : String,
    override val publisher: AppUser,
    val video: Int,
    val content: String,
    val times: String,
    val replies: List<Reply>
) :
    Publication(
        id,
        publisher
    )




