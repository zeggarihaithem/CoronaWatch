package solutus.coronawatch.ui.loginActivity.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.coronawatch_mobile.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import solutus.coronawatch.TokenApp
import solutus.coronawatch.data.entity.AppUser
import solutus.coronawatch.data.internal.GetDataFromApiException
import solutus.coronawatch.data.network.NetworkConnexion
import solutus.coronawatch.data.network.entity.Token
import solutus.coronawatch.data.network.implementation.UserApi
import solutus.coronawatch.data.reposetory.implementation.UserRepository
import solutus.coronawatch.ui.mainActivity.MainActivity


class LoginFragment : Fragment()  , KodeinAware{


    override val kodein by closestKodein()

    private val userRepository =
        UserRepository(UserApi.invoke())
    private var emailPassword = HashMap<String,String>()
    private lateinit var token : String
    private var user : AppUser? = null
    companion object {
        fun newInstance() = LoginFragment()
    }

    private val networkConnexion: NetworkConnexion by instance<NetworkConnexion>()
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        networkConnexion.observe(viewLifecycleOwner, Observer { isOnline ->
            if (isOnline) {
                error.visibility = View.GONE
                enter.isEnabled = true
                enter.setOnClickListener {
                    if (email.text.toString() == "" || password.text.toString() == "") {
                        Toast.makeText(activity, "يجب ملأ كل الحقول", Toast.LENGTH_SHORT).show()
                    } else {
                        //TODO :login
                        emailPassword["email"] = email.text.toString().trim()
                        emailPassword["password"] = password.text.toString().trim()
                        CoroutineScope(IO).launch {
                            loginWithEmail()
                            apiRequest()
                        }
                    }
                }

                loginFromFacebook()
            }
            else{
                enter.isEnabled = false
                setErrorMessage("جهازك غير متصل بالانترنت")
            }
        })


    }

    private fun loginFromFacebook(){

        login_Facebook.fragment = this
        login_Facebook.setLoginText("عبر الفايسبوك")
        login_Facebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                progress_bar.visibility = View.VISIBLE
                Log.d(
                    "Debug", "User ID: " + loginResult.accessToken
                    .userId + "\n" + "Auth Token: " + loginResult.accessToken
                        .token
                )
                CoroutineScope(IO).launch {
                    try {
                        token = userRepository.getUserFromFacebook(loginResult.accessToken).token
                        apiRequest()
                    } catch (e: Exception) {
                        Log.d("Debug", e.message)
                    }
                }
            }
            override fun onCancel() {
                println("Hey I'm a facebook cancel")
            }

            override fun onError(e: FacebookException) {
                println("Hey I'm a facebook error")
            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode , resultCode , data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private suspend fun loginWithEmail() {
        this.token = userRepository.loginUser(this.emailPassword)!!.token
    }

    private suspend fun apiRequest() {
        try {
            this.user = getUser(this.token)
            TokenApp.token = Token("Token $token")
            user?.let { showToken(it) }
        }catch (e : GetDataFromApiException){
            println("Network call exception ${e.message}")
            setErrorMessage("البريد أو كلمة السر خاطئة")
        }
    }

    private fun setErrorMessage(message : String ){
        CoroutineScope(Main).launch{
            error.text = message
            error.visibility = View.VISIBLE
        }
    }

    private suspend fun showToken(user: AppUser) {
        withContext(Main) {
            MainActivity.isLogin.value = true
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("token",token)
            intent.putExtra("user",user)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private suspend fun getUser(token: String): AppUser? {
        return userRepository.getAuthAppUser("token $token")
    }

}
