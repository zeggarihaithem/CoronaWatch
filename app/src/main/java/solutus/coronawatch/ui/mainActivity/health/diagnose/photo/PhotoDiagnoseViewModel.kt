package solutus.coronawatch.ui.mainActivity.health.diagnose.photo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel

class PhotoDiagnoseViewModel : ViewModel() {
    var photoPath: String? = null
    fun uploadData(context : Context){
        Toast.makeText(context, "$photoPath", Toast.LENGTH_SHORT).show()
        //TODO : Completer l'integration avec l'api ici
    }
}
