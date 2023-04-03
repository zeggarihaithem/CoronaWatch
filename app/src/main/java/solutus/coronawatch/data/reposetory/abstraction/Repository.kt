package solutus.coronawatch.data.reposetory.abstraction

import solutus.coronawatch.data.network.abstraction.Api
import solutus.coronawatch.data.network.entity.Token

abstract class Repository (
    val api : Api
) : SafeApiRequest() {

     val token : Token = Companion.getToken()

    companion object {

        /**
         * get the token from the cache , or from the network and put it on the cache
         * */
        fun getToken(): Token {
            TODO( )
        }
    }


}