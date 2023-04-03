package solutus.coronawatch.ui.mainActivity.info

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.info_fragment.*
import solutus.coronawatch.ui.mainActivity.MainActivity


class InfoFragment : Fragment() {

    companion object {
        fun newInstance() = InfoFragment()
        const val ACCESS_FINE_LOCATION_REQUEST_CODE = 100
    }

    private lateinit var navController: NavController
    private var locationManager: LocationManager? = null
    private var listener: LocationListener? = null
    private lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        deleteProgressBar()
        //set the navigation menu
        navController = activity.let { Navigation.findNavController(it, R.id.nav_info_fragment) }
        info_navigation.setupWithNavController(navController)

        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //request for gps permission
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(activity, "يرجى تفعيل نظام تحديد المواقع الخاص بك", Toast.LENGTH_LONG)
                .show()
        }
        requestGpsPermission()

        //get gps location
        listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                InfoFragmentViewModel.location.x = location.latitude.toFloat()
                InfoFragmentViewModel.location.y = location.altitude.toFloat()

            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
            override fun onProviderEnabled(s: String) {}
            override fun onProviderDisabled(s: String) {
                try {
                    val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(i)
                } catch (e: IllegalStateException) {
                    Toast.makeText(
                        activity,
                        "يرجى تفعيل نظام تحديد المواقع الخاص بك",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                10F,
                listener!!
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    private fun requestGpsPermission() {
        val permissions =
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, ACCESS_FINE_LOCATION_REQUEST_CODE)
        }
    }

    private fun checkPermission(permission: String): Boolean {
        val check = ContextCompat.checkSelfPermission(requireActivity(), permission)
        return (check == PackageManager.PERMISSION_GRANTED)
    }

    fun showProgressBar(){
        progress_report.visibility = View.VISIBLE
    }

    fun deleteProgressBar(){
        progress_report.visibility = View.GONE
    }

}
