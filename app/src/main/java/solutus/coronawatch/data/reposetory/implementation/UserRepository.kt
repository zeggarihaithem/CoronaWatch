package solutus.coronawatch.data.reposetory.implementation


import com.facebook.AccessToken
import solutus.coronawatch.data.entity.AppUser
import solutus.coronawatch.data.entity.RegisterPostRequest
import solutus.coronawatch.data.network.entity.Token
import solutus.coronawatch.data.internal.GetDataFromApiException
import solutus.coronawatch.data.reposetory.abstraction.SafeApiRequest
import solutus.coronawatch.data.network.implementation.UserApi

class UserRepository(
    private val userApi: UserApi
) : SafeApiRequest() {



    suspend fun getAuthAppUser(token : String) : AppUser?  {
        return  userApi.getAuthUser(token).body()
    }

    suspend fun getUserFromFacebook(accessToken: AccessToken): Token {

        println("Facebook Token ${accessToken.token}")
        val hashMap = HashMap<String , String>()
        hashMap["access_token"] = accessToken.token
       return apiRequest { userApi.loginWithFacebook( hashMap) }
    }



    suspend fun getUser(id:Int) : AppUser{
        return apiRequest { userApi.getUser(id) }

    }

    @Throws(GetDataFromApiException::class)
    suspend fun registerUser(email : String,first_name:String,last_name:String,password:String,second_password:String) : AppUser?{
        val postRequest = RegisterPostRequest(email,first_name,last_name,password,second_password)
        return apiRequest {  userApi.addUser(postRequest) }
    }

    @Throws(GetDataFromApiException::class)
    suspend fun loginUser(emailPassword : HashMap<String,String>): Token? =
        apiRequest { userApi.loginUser(emailPassword) }
}