package solutus.coronawatch.data.cache.entity

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable


 data class Video(

     override  val id : String,
     override  val publisher : AppUser,
     //override var comments: List<Comment>?,
     var title : String,
     var content : String,
     var url : String,
     var thumbnail : String

 ) :
     Publication(
         id,
         publisher

         //,comments
     )



