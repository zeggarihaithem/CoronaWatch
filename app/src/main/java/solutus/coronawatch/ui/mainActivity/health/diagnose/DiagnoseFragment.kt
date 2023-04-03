package solutus.coronawatch.ui.mainActivity.health.diagnose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.diagnose_fragment.*


class DiagnoseFragment : Fragment() {

    companion object {
        fun newInstance() = DiagnoseFragment()
        const val TEMPERATURE_MIN = 30
        const val TEMPERATURE_MAX = 45
        const val HEART_BEAT_MIN = 40
        const val HEART_BEAT_MAX = 200
        const val WEIGHT_MIN = 1
        const val WEIGHT_MAX = 250
    }


    private lateinit var viewModel: DiagnoseViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.diagnose_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DiagnoseViewModel::class.java)

        //set the navigation menu
        navController =
            activity?.let { Navigation.findNavController(it, R.id.nav_diagnose_fragment) }!!
        diagnose_navigation.setupWithNavController(navController)

        //temperature seek bar
        temperature_seek_bar.progress = 0
        temperature_seek_bar.max = TEMPERATURE_MAX - TEMPERATURE_MIN
        temperature_seek_bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                b: Boolean
            ) {
                val temperature = progress + TEMPERATURE_MIN
                temperature_value_view.text = temperature.toString()
                viewModel.temperature = temperature
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        //heart beat seek bar
        heart_beat_seek_bar.progress = 0
        heart_beat_seek_bar.max = HEART_BEAT_MAX - HEART_BEAT_MIN
        heart_beat_seek_bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                b: Boolean
            ) {
                val heartBeat = progress + HEART_BEAT_MIN
                heart_beat_value_view.text = heartBeat.toString()
                viewModel.heartBeat = heartBeat

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        //weight number piker
        weight_number_piker.minValue = WEIGHT_MIN
        weight_number_piker.maxValue = WEIGHT_MAX
        weight_number_piker.value = WEIGHT_MIN
        weight_number_piker.setOnValueChangedListener { _, _, newVal ->
            viewModel.weight = newVal
        }

        //diagnose on button click
        diagnose_button.setOnClickListener {
            diagnose()
        }
    }

    private fun diagnose() {
        viewModel.uploadData(requireContext())
        toResultFragment()

    }

    private fun toResultFragment() {
        val diagnoseNavController =
            Navigation.findNavController(requireActivity(), R.id.nav_health_fragment)
        diagnoseNavController.navigate(R.id.to_result_fragment_action)
    }

}
