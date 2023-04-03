package com.example.coronawatch_mobile.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import solutus.coronawatch.data.entity.AppUser
import solutus.coronawatch.data.network.implementation.UserApi
import solutus.coronawatch.data.reposetory.implementation.UserRepository

class UserRepositoryTest{
    private val userRepository =
        UserRepository(UserApi.invoke())

    @Test
     fun getAppUserTest() = runBlocking{
       val appUser =  userRepository.getAuthAppUser("token 63c80d8178352bd3e9cfa68e21d82fd1991a0251")

       Assert.assertNotNull(appUser)
    }

  /*  @Test
    fun addUserTest() = runBlocking{

        val user  = AppUser(
            1324,
            "Mohamed",
            "Mazrou",
            "26-04-2000",
            "g@gmail.com",
            "23-4-2020",
            "23-4-2020",
            true,
            2,
            "nitrou"
        )
        val appUser =  userRepository.addUser(user)

        Assert.assertEquals(appUser , null)
    }
*/
    @Test
    fun getUserById() = runBlocking {
      lateinit var user : AppUser
      val userRepository =
          UserRepository(
              UserApi.invoke()
          )
      user = userRepository.getUser(14)
      println(user.firstName)
      Assert.assertEquals(user.email,"hamid@email.com")
  }

    @Test
    fun loginUserTest() = runBlocking {
        val emailPassword = HashMap<String , String>()
        emailPassword["email"] = "ga_mazrou@esi.dz"
        emailPassword["password"] = "nitrou18b_boy"
        Assert.assertEquals("f50f1067a2f33109abbf197f165971678c460d02", userRepository.loginUser(emailPassword)!!.token)
    }
}