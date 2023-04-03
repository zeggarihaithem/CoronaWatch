package solutus.coronawatch.ui.mainActivity.health.diagnose.camera

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel

class CameraDiagnoseViewModel : ViewModel() {
    var photoPath: String? = null
    fun uploadData(context : Context){
        Toast.makeText(context, "$photoPath", Toast.LENGTH_SHORT).show()
        //TODO : Completer l'integration avec l'api ici
    }
}
