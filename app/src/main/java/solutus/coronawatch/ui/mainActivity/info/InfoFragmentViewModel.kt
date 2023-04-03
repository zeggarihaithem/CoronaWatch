package solutus.coronawatch.ui.mainActivity.info

import androidx.lifecycle.ViewModel
import solutus.coronawatch.data.entity.Location

class InfoFragmentViewModel  : ViewModel() {
    companion object {
         var location =  Location(0f,0f)
    }
}