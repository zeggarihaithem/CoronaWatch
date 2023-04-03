package solutus.coronawatch.ui.mainActivity.info.photo

import android.content.Context
import android.net.Uri
import android.os.FileUtils
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import solutus.coronawatch.data.entity.Case

import solutus.coronawatch.data.internal.GetDataFromApiException
import solutus.coronawatch.data.network.entity.Token
import solutus.coronawatch.data.network.implementation.ReportApi
import solutus.coronawatch.data.reposetory.implementation.ReportRepository
import solutus.coronawatch.utilities.RealPathUtil
import java.io.File

import solutus.coronawatch.ui.mainActivity.info.InfoFragmentViewModel
import solutus.coronawatch.ui.mainActivity.info.OnSubmitListener


class PhotoInfoViewModel(
    var repository : ReportRepository
) : ViewModel() {
    var photoPath: String? = null
    var photoUri: Uri? = null

    var onSubmitListener : OnSubmitListener? = null

    fun uploadCase(context: Context, description: String) {

        val case = Case(description, InfoFragmentViewModel.location, photoPath!!, "IMAGE")

        try {
            val realPath: String? = RealPathUtil.getRealPath(context, photoUri!!)
            val originalFile: File = File(realPath!!)
            val str = context.contentResolver?.getType(photoUri!!) as String
            val file: RequestBody = RequestBody.create(str.toMediaTypeOrNull(), originalFile)


            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("attachment", originalFile.name, file)

            var success = false
          val job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    println("Debug : Sending the file on the network")
                    repository.reportImage(description, image, case.location)
                    onSubmitListener?.onSubmit(true)
                    success = true
                } catch (e: Exception) {
                    println("Debug : ${e.message}")
                    e.printStackTrace()
                    onSubmitListener?.onSubmit(false)
                }
            }
        } catch (e: Exception) {
            println("Debug : ${e.cause}")
            e.printStackTrace()
        }
    }
}