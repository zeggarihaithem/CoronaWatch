package solutus.coronawatch.data.entity

data class Reply(
    override val id: String,
    override val publisher: AppUser,
    val video: Int,
    val content: String,
    val times: String,
    val parent: Int
) :
    Publication(
        id,
        publisher
    )
