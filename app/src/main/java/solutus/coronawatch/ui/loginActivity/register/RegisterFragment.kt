package solutus.coronawatch.ui.loginActivity.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
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

class RegisterFragment : Fragment() , KodeinAware {
    override val kodein by closestKodein()
    private val userRepository =
        UserRepository(UserApi.invoke())
    private var user : AppUser? = null
    private var emailPassword = HashMap<String,String>()
    private lateinit var token : String

    private val networkConnexion: NetworkConnexion by instance()

    companion object {
        fun newInstance() =
            RegisterFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        networkConnexion.observe(viewLifecycleOwner , Observer {isOnline->
            if(isOnline){
                error.visibility = View.GONE
                register.isEnabled = true
                register.setOnClickListener {
                    if(agree.isChecked){
                        if(email.text.toString()==""||first_name.text.toString()==""||second_name.text.toString()==""||password.text.toString()==""||confirm_password.text.toString()==""){
                            Toast.makeText(activity,"يجب ملأ كل الحقول",Toast.LENGTH_SHORT).show()
                        }else if (! isEmailValid(email.text.toString()) ){
                            Toast.makeText(activity,"أدخل ايميل صحيح من فضلك",Toast.LENGTH_SHORT).show()
                        }else if(confirm_password.text.toString() != password.text.toString()){
                            Toast.makeText(activity,"أعد كتابة كلة السر",Toast.LENGTH_SHORT).show()
                            confirm_password.setText("")
                            password.setText("")
                        }
                        else{
                            //TODO:Registre un user

                            val email = email.text.toString()
                            val fName = first_name.text.toString()
                            val lName = second_name.text.toString()
                            val firstPassword= password.text.toString()
                            val secondPassword = confirm_password.text.toString()
                            emailPassword["email"] = email
                            emailPassword["password"] = firstPassword
                            CoroutineScope(IO).launch {
                                try {
                                    user = userRepository.registerUser(email,fName,lName,firstPassword,secondPassword)
                                    token = userRepository.loginUser(emailPassword)!!.token
                                    login(user!!,token)
                                    TokenApp.token = Token("token $token")
                                }catch (e : GetDataFromApiException){
                                    setErrorMessage("هذا البريد الاكتروني مستعمل من قبل")
                                }catch (e : Exception){
                                    setErrorMessage("هذا البريد الاكتروني مستعمل من قبل")
                                    println("debug message ${e.message}")
                                }
                            }
                        }
                    }else{
                        Toast.makeText(activity,"يجب ان توافق على شروط الاستخدام",Toast.LENGTH_SHORT).show()
                    }
                }
       /*         condition.setOnClickListener {
                    //TODO: afficher les condition d'utlisation
                }*/
            }else{
                register.isEnabled = false
                setErrorMessage("جهازك غير متصل بالانترنت")
            }
        })

    }


    private fun setErrorMessage(message : String ){

        CoroutineScope(Dispatchers.Main).launch{
            error.text = message
            error.visibility = View.VISIBLE
        }

    }
    suspend fun login(user : AppUser, token : String){
        withContext(Dispatchers.Main){
            MainActivity.isLogin.value = true
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("token",token)
            intent.putExtra("user",user)
            startActivity(intent)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        println("is $email a valid email ${android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()}")
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
