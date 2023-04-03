package solutus.coronawatch.data.entity

import com.google.gson.annotations.SerializedName
import retrofit2.http.Part

data class RegisterPostRequest (
    @SerializedName("email")
    val email : String,
    @SerializedName("first_name")
    val first_name : String,
    @SerializedName("last_name")
    val last_name : String,
    @SerializedName("password")
    val password : String,
    @SerializedName("password2")
    val second_password : String
)