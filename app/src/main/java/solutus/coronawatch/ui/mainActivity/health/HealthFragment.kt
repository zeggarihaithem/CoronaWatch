package solutus.coronawatch.ui.mainActivity.health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.health_fragment.*


class HealthFragment : Fragment() {

    companion object {
        fun newInstance() = HealthFragment()
    }

    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.health_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        diagnose_view.setOnClickListener {
            navController =
                Navigation.findNavController(requireActivity(), R.id.nav_health_fragment)
            navController.navigate(R.id.to_diagnose_fragment_action)
            diagnose_view.setBackgroundResource(R.drawable.ic_health_red_button)
            result_view.setBackgroundResource(R.drawable.ic_health_white_button)
            diagnose_view.setTextColor(resources.getColor(R.color.fontColorPrimary))
            result_view.setTextColor(resources.getColor(R.color.colorPrimary))

        }
        result_view.setOnClickListener {
            navController =
                Navigation.findNavController(requireActivity(), R.id.nav_health_fragment)
            navController.navigate(R.id.to_result_fragment_action)
            diagnose_view.setBackgroundResource(R.drawable.ic_health_white_button)
            result_view.setBackgroundResource(R.drawable.ic_health_red_button)
            diagnose_view.setTextColor(resources.getColor(R.color.colorPrimary))
            result_view.setTextColor(resources.getColor(R.color.fontColorPrimary))
        }

    }

}
