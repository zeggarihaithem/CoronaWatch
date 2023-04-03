package solutus.coronawatch.data.reposetory.abstraction

import solutus.coronawatch.data.entity.AppUser
import solutus.coronawatch.data.entity.RegisterPostRequest
import solutus.coronawatch.data.network.entity.Token

interface UserRepository {

    suspend fun  getAuthAppUser() : AppUser?

    suspend fun getUser(id:Int) : AppUser

    suspend fun registerUser(postRequest : RegisterPostRequest) : AppUser?

    suspend fun loginUser(emailPassword : HashMap<String,String>): Token?

    suspend fun logOut (token : Token)

}