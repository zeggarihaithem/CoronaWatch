package solutus.coronawatch.data.entity


data class Video(

    override  val id : String,
    override  val publisher : AppUser,

    var title : String,
    var content : String,
    var url : String,
    var thumbnail: String
) :
     Publication(
         id,
         publisher

     )



