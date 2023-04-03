package solutus.coronawatch.ui.mainActivity.health.diagnose

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel

class DiagnoseViewModel : ViewModel() {
    var temperature: Int = DiagnoseFragment.TEMPERATURE_MIN
    var heartBeat: Int = DiagnoseFragment.HEART_BEAT_MIN
    var weight: Int = DiagnoseFragment.WEIGHT_MIN

    fun uploadData(context : Context){
        Toast.makeText(context, "$temperature  $heartBeat $weight", Toast.LENGTH_SHORT).show()
        //TODO : Completer l'integration avec l'api ici
    }

}
