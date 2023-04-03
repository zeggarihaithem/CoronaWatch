package solutus.coronawatch.data.network.entity

import android.accounts.AuthenticatorDescription
import okhttp3.MultipartBody
import solutus.coronawatch.data.entity.Location
import java.io.File

data class CaseToReport(
     val description: String ,
     val  location: Location ,
     val attachment : MultipartBody.Part
)