package solutus.coronawatch.ui.mainActivity.info.camera

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import solutus.coronawatch.data.entity.Case

import solutus.coronawatch.data.reposetory.implementation.ReportRepository
import solutus.coronawatch.utilities.RealPathUtil
import java.io.File

import solutus.coronawatch.ui.mainActivity.info.InfoFragmentViewModel


class CameraInfoViewModel(
    val repository: ReportRepository
) : ViewModel() {
    var mediaPath: String? = null
    var mediaUri : Uri? = null
    var type: String? = null

        fun uploadCase(context: Context, description: String) {

            val case = Case(description, InfoFragmentViewModel.location, mediaPath!! , type!!)
            try {
                //Toast.makeText(context, "$photoPath  $description", Toast.LENGTH_SHORT).show()
                val realPath :String? = RealPathUtil.getRealPath(context,mediaUri!!)
                val originalFile : File = File(realPath!!)
                val str = context.contentResolver?.getType(mediaUri!!) as String
                val file : RequestBody = RequestBody.create(str.toMediaTypeOrNull(),originalFile)


                val image : MultipartBody.Part = MultipartBody.Part.createFormData("attachment",originalFile.name,file )
                var success = false
                val job = CoroutineScope(Dispatchers.IO).launch {
                    try {
                        println("Debug : Sending the file on the network")
                        repository.reportImage(description , image , case.location)
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, " تم الابلاغ بنجاحا", Toast.LENGTH_LONG).show()
                        }

                        success = true
                    }catch (e : Exception){
                        println("Debug : ${e.message}")
                        e.printStackTrace()
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, " لم يتم الابلاغ بنجاح يرجى المحاولة مجددا  ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                if(job.isCompleted){
                    if(success)
                        Toast.makeText(context, " تم الابلاغ بنجاحا", Toast.LENGTH_LONG).show()
                }

            }catch (e : Exception){
                println("Debug : ${e.cause}")
                e.printStackTrace()
            }

        }



}
