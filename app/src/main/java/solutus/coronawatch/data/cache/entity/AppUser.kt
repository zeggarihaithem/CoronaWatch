package solutus.coronawatch.data.cache.entity


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppUser  (
    @SerializedName("pk")
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("birth_date")
    val birthDate: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("last_login")
    val lastLogin: Any,

    @SerializedName("date_joined")
    val dateJoined: String,

    @SerializedName("is_active")
    val isActive: Boolean, // true

    @SerializedName("role")
    val role: Int,

    @SerializedName("image")
    val image : String


) : Serializable