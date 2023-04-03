package solutus.coronawatch.data.reposetory.implementation


import okhttp3.MultipartBody
import okhttp3.RequestBody

import solutus.coronawatch.TokenApp
import solutus.coronawatch.data.entity.Location
import solutus.coronawatch.data.network.entity.CaseToReport
import solutus.coronawatch.data.network.implementation.ReportApi
import solutus.coronawatch.data.reposetory.abstraction.SafeApiRequest


class ReportRepository  (
   private val api  : ReportApi
) : SafeApiRequest() {




    suspend fun reportImage(description: String, image: MultipartBody.Part, location : Location) {
        println("Debug : ${TokenApp.token}")
        val caseToReport =
            CaseToReport(
                description = description,
                attachment = image,
                location = location
            )
        //println("Debug : ${image.body} , with size of : ${image.headers}")
        val descriptionApi = RequestBody.create(
            MultipartBody.FORM, description
        )
        val multipartBody  = MultipartBody.Builder()
            .addPart(descriptionApi)
            .addPart(image)
            .build()

        apiRequest {
            api.reportCase(
                TokenApp.token!!.token,
                descriptionApi ,
                x = location.x ,
                y = location.y,
                attachment = image
             //   description,
              //  image
            )
        }

    /*    tetApi.testRequest(
            image ,
            descriptionApi
        )*/
    }
}