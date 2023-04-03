package solutus.coronawatch.ui.mainActivity.map

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.coronawatch_mobile.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCircleClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.filter_dialog.*
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import solutus.coronawatch.data.entity.InternationalZone
import solutus.coronawatch.data.entity.InternationalZoneInfos
import solutus.coronawatch.data.entity.NationalZone
import solutus.coronawatch.data.entity.NationalZoneInfos
import solutus.coronawatch.data.network.implementation.MapApi
import solutus.coronawatch.data.reposetory.implementation.MapRepository


class MapFragment : Fragment() , OnMapReadyCallback{

    var is_national = false
    var zoom : Float = 0.0f
    private  lateinit var googlemMap : GoogleMap
    var nationalZoneInfos = HashMap<Circle,NationalZoneInfos>()
    var internationalZoneInfos = HashMap<Circle,InternationalZoneInfos>()
    lateinit var nationalZones : ArrayList<NationalZone>
    lateinit var interNationalZones : ArrayList<InternationalZone>
    var nationalCircles = ArrayList<Circle>()
    var internationalCircles = ArrayList<Circle>()
    var nationalCentresCircles = ArrayList<Circle>()
    lateinit var btn : MaterialButton
    //filters
    var RECOVERED_FILTER = 1
    var DEAD_FILTER = 2
    var SICK_FILTER = 3
    var INFECTED_FILTER = 4
    var checkedBtn = 2



    private val mapRepository =
        MapRepository(
            MapApi()
        )

    companion object {
        fun newInstance() = MapFragment()
    }


    private lateinit var viewModel: MapViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        map_view .onCreate(savedInstanceState)
        map_view.onResume()
        map_view.getMapAsync(this)
        viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        // TODO: Use the ViewModel

        filterbtn.setOnClickListener{
            showFilter()
        }

        national.setOnClickListener {
            if (!is_national) {
                for (circle in internationalCircles) circle.isVisible=false
                if (nationalCircles.isEmpty()) {
                    for (zone in nationalZones) {
                        var circle = googlemMap.addCircle(
                            CircleOptions()
                                .center(LatLng(zone.x, zone.y))
                                .radius(100.0 * zone.dead)
                                .fillColor(Color.argb(55, 255, 0, 0))
                                .strokeColor(Color.argb(95, 255, 0, 0))
                                .strokeWidth(3f)
                                .clickable(true)
                        )
                        if (zone.centre == false) {
                            nationalCircles.add(circle)
                            nationalZoneInfos.put(
                                circle,
                                NationalZoneInfos(
                                    zone.name,
                                    zone.dead,
                                    zone.sick,
                                    zone.recovered,
                                    zone.infected,
                                    zone.is_risky,
                                    zone.remarque,
                                    zone.centre
                                )
                            )
                        }
                        else {
                            nationalCentresCircles.add(circle)
                            nationalZoneInfos.put(
                                circle,
                                NationalZoneInfos(
                                    zone.name,
                                    zone.dead,
                                    zone.sick,
                                    zone.recovered,
                                    zone.infected,
                                    zone.is_risky,
                                    zone.remarque,
                                    zone.centre
                                )
                            )
                            }
                     }
                }else{

                    for (circle in nationalCentresCircles) circle.isVisible=true

                }
                            is_national = true
                            national.setText(R.string.national)
                            val ALGERIA = LatLngBounds(
                                LatLng(18.326717, -9.300572), LatLng(37.980762, 12.231325)
                            )
                            googlemMap.setLatLngBoundsForCameraTarget(ALGERIA)
                            googlemMap.setMaxZoomPreference(15f)
                            googlemMap.setMinZoomPreference(5.5f)
                            googlemMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                    context,
                                    R.raw.national_map_style
                                )
                            )
                            googlemMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    ALGERIA.center,
                                    5.5F
                                )
                            )


            }else{
                for (circle in nationalCentresCircles) circle.isVisible=false
                for (circle in nationalCircles) circle.isVisible=false
                for (circle in internationalCircles) circle.isVisible=true
                is_national=false
                national.setText(R.string.international)
                googlemMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.context, R.raw.map_style))
                googlemMap.setLatLngBoundsForCameraTarget(null)
                googlemMap.setMaxZoomPreference(4f)
                googlemMap.setMinZoomPreference(0f)
                googlemMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLngBounds(LatLng(0.0,0.0),
                    LatLng(0.0,0.0)).center, 0f))
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.context, R.raw.map_style))
            CoroutineScope(IO).launch {
                interNationalZones = mapRepository.getInterNationalZones()!!
                nationalZones = mapRepository.getNationalZones()!!

                withContext(Main){
                    for (zone in interNationalZones){
                        var circle = map.addCircle(
                            CircleOptions()
                                .center(LatLng(zone.x, zone.y))
                                .radius(10.0 * zone.dead)
                                .fillColor(Color.argb(55, 255, 0, 0))
                                .strokeColor(Color.argb(95, 255, 0, 0))
                                .strokeWidth(3f)
                                .clickable(true)
                        )
                        internationalCircles.add(circle)
                        internationalZoneInfos.put(circle,
                            InternationalZoneInfos(zone.name,zone.dead,zone.sick,zone.recovered,zone.infected,zone.continent)
                        )
                    }
                    map.setMaxZoomPreference(4f)
                    map.setMinZoomPreference(0f)
                    map.setOnCircleClickListener(OnCircleClickListener { circle ->
                        // Flip the r, g and b components of the circle's
                        // stroke color.
                        showInfo(circle)
                    })
                    map.setOnCameraIdleListener {
                       if (is_national){
                            zoom = map.cameraPosition.zoom
                            if (zoom > 9.0f){
                                for (centre in nationalCentresCircles) centre.isVisible = false
                                for (zone in nationalCircles) zone.isVisible = true
                            }
                           else{
                                for (centre in nationalCentresCircles) centre.isVisible = true
                                for (zone in nationalCircles) zone.isVisible = false
                            }
                        }
                    }
                    googlemMap = map
                }

            }
        }
    }


    fun showInfo(circle : Circle){
        val dialog = context?.let { Dialog(it) }
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.show_info_dialog)
        if (!is_national){
            dialog!!.findViewById<TextView>(R.id.stateName).setText(internationalZoneInfos[circle]!!.name)
            dialog!!.findViewById<TextView>(R.id.deaths).setText(internationalZoneInfos[circle]!!.dead.toString())
            dialog!!.findViewById<TextView>(R.id.recovered).setText(internationalZoneInfos[circle]!!.recovered.toString())
            dialog!!.findViewById<TextView>(R.id.sick).setText(internationalZoneInfos[circle]!!.sick.toString())
            dialog!!.findViewById<TextView>(R.id.infected).setText(internationalZoneInfos[circle]!!.infected.toString())
        }
        else{
            dialog!!.findViewById<TextView>(R.id.stateName).setText(nationalZoneInfos[circle]!!.name)
            dialog!!.findViewById<TextView>(R.id.deaths).setText(nationalZoneInfos[circle]!!.dead.toString())
            dialog!!.findViewById<TextView>(R.id.recovered).setText(nationalZoneInfos[circle]!!.recovered.toString())
            dialog!!.findViewById<TextView>(R.id.sick).setText(nationalZoneInfos[circle]!!.sick.toString())
            dialog!!.findViewById<TextView>(R.id.infected).setText(nationalZoneInfos[circle]!!.infected.toString())
        }

        dialog!!.show()
    }

    fun showFilter(){
        val dialog = context?.let{Dialog(it)}
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.filter_dialog)
        dialog?.show()
        btn = dialog!!.findViewById(R.id.confirmFilter)
        val radiogrp = dialog!!.findViewById<RadioGroup>(R.id.radioGroup)

        if (checkedBtn == 1) radiogrp.check(dialog!!.findViewById<RadioButton>(R.id.recoveredFilter).id)
        else if (checkedBtn == 2) radiogrp.check(dialog!!.findViewById<RadioButton>(R.id.deathsFilter).id)
        else if (checkedBtn == 3) radiogrp.check(dialog!!.findViewById<RadioButton>(R.id.sickFilter).id)
        else if (checkedBtn == 4) radiogrp.check(dialog!!.findViewById<RadioButton>(R.id.infectedFilter).id)



        btn.setOnClickListener {
            if(radiogrp.checkedRadioButtonId == dialog!!.findViewById<RadioButton>(R.id.recoveredFilter).id) {
                radiogrp.check(dialog!!.findViewById<RadioButton>(R.id.recoveredFilter).id)
                checkedBtn = RECOVERED_FILTER
                funFilter(RECOVERED_FILTER)
            }
            else if (radiogrp.checkedRadioButtonId == dialog!!.findViewById<RadioButton>(R.id.deathsFilter).id){
                radiogrp.check(dialog!!.findViewById<RadioButton>(R.id.deathsFilter).id)
                checkedBtn = DEAD_FILTER
                funFilter(DEAD_FILTER)
            }
            else if (radiogrp.checkedRadioButtonId == dialog!!.findViewById<RadioButton>(R.id.sickFilter).id){
                radiogrp.check(dialog!!.findViewById<RadioButton>(R.id.sickFilter).id)
                checkedBtn = SICK_FILTER
                funFilter(SICK_FILTER)
            }
            else if (radiogrp.checkedRadioButtonId == dialog!!.findViewById<RadioButton>(R.id.infectedFilter).id)  {
                radiogrp.check(dialog!!.findViewById<RadioButton>(R.id.infectedFilter).id)
                checkedBtn = INFECTED_FILTER
                funFilter(INFECTED_FILTER)
            }

        }
    }

    fun funFilter(filter :Int) {

        if (!is_national) {
            if (filter == RECOVERED_FILTER) {
                for (circle in internationalCircles){
                    circle.fillColor = Color.argb(69, 0, 253, 0)
                    circle.strokeColor = Color.argb(100,0,197,0)
                    circle.radius = 1.0 * internationalZoneInfos[circle]!!.recovered
                }

            }
            if (filter == DEAD_FILTER) {
                for (circle in internationalCircles){
                    circle.fillColor = Color.argb(55, 255, 0, 0)
                    circle.strokeColor = Color.argb(95, 255, 0, 0)
                    circle.radius = 10.0 * internationalZoneInfos[circle]!!.dead
                }

            }
            if (filter == SICK_FILTER) {
                for (circle in internationalCircles){
                    circle.fillColor = Color.argb(100, 255,178,0)
                    circle.strokeColor = Color.argb(100, 255,157,0)
                    circle.radius = 1.0 * internationalZoneInfos[circle]!!.sick
                }

            }
            if (filter == INFECTED_FILTER) {
                for (circle in internationalCircles){
                    circle.fillColor = Color.argb(100, 255,255,107)
                    circle.strokeColor = Color.argb(100, 255,255,29)
                    circle.radius = 1.0 * internationalZoneInfos[circle]!!.infected
                }

            }
        }

        else
        {
            if (googlemMap.cameraPosition.zoom > 9.0f)
            {
            if (filter == RECOVERED_FILTER) {
                for (circle in nationalCircles) {
                    circle.fillColor = Color.argb(69, 0, 253, 0)
                    circle.strokeColor = Color.argb(100, 0, 197, 0)
                    circle.radius = 10.0 * nationalZoneInfos[circle]!!.recovered
                }

            }
            if (filter == DEAD_FILTER) {
                for (circle in nationalCircles) {
                    circle.fillColor = Color.argb(55, 255, 0, 0)
                    circle.strokeColor = Color.argb(95, 255, 0, 0)
                    circle.radius = 100.0 * nationalZoneInfos[circle]!!.dead
                }

            }
            if (filter == SICK_FILTER) {
                for (circle in nationalCircles) {
                    circle.fillColor = Color.argb(100, 255, 178, 0)
                    circle.strokeColor = Color.argb(100, 255, 157, 0)
                    circle.radius = 10.0 * nationalZoneInfos[circle]!!.sick
                }

            }
            if (filter == INFECTED_FILTER) {
                for (circle in nationalCircles) {
                    circle.fillColor = Color.argb(100, 255, 255, 107)
                    circle.strokeColor = Color.argb(100, 255, 255, 29)

                    circle.radius = 10.0 * nationalZoneInfos[circle]!!.infected
                    }

                }
            }else {
                if (filter == RECOVERED_FILTER) {
                    for (circle in nationalCentresCircles) {
                        circle.fillColor = Color.argb(69, 0, 253, 0)
                        circle.strokeColor = Color.argb(100, 0, 197, 0)
                        circle.radius = 10.0 * nationalZoneInfos[circle]!!.recovered
                    }

                }
                if (filter == DEAD_FILTER) {
                    for (circle in nationalCentresCircles) {
                        circle.fillColor = Color.argb(55, 255, 0, 0)
                        circle.strokeColor = Color.argb(95, 255, 0, 0)
                        circle.radius = 100.0 * nationalZoneInfos[circle]!!.dead
                    }

                }
                if (filter == SICK_FILTER) {
                    for (circle in nationalCentresCircles) {
                        circle.fillColor = Color.argb(100, 255, 178, 0)
                        circle.strokeColor = Color.argb(100, 255, 157, 0)
                        circle.radius = 10.0 * nationalZoneInfos[circle]!!.sick
                    }

                }
                if (filter == INFECTED_FILTER) {
                    for (circle in nationalCentresCircles) {
                        circle.fillColor = Color.argb(100, 255, 255, 107)
                        circle.strokeColor = Color.argb(100, 255, 255, 29)
                        circle.radius = 10.0 * nationalZoneInfos[circle]!!.infected
                    }

                }

            }
        }
    }


}
