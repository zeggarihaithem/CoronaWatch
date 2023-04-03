package solutus.coronawatch.data.entity


data class Article(

    override val id: String,
    override val publisher: AppUser,
    var title: String,
    var url: String


) :
    Publication(
        id,
        publisher
    )



