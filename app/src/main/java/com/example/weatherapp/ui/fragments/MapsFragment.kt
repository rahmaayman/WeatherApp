package com.example.weatherapp.ui.fragments

import android.content.SharedPreferences
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weatherapp.R
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.weatherapp.ui.fragments.FavouritesFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() ,GoogleMap.OnMapClickListener{
    private lateinit var mMap : GoogleMap
    private  var lat: Double=0.0
    private  var lon: Double=0.0
    lateinit var prefs: SharedPreferences

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        this.mMap=googleMap
       // mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
        mMap.setOnMapClickListener(this)
        /*googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        //Toast.makeText(requireActivity(), "${arguments?.getInt("mapId")} ", Toast.LENGTH_LONG).show()
    }

    override fun onMapClick(point: LatLng) {
        Toast.makeText(requireActivity(), "${point.latitude.toString()},${point.longitude.toString()}", Toast.LENGTH_LONG).show()
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(point))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point))
        lat=point.latitude
        lon= point.longitude
        if (lat==0.0 || lon==0.0) {
            Toast.makeText(requireActivity(), "please select place , ", Toast.LENGTH_LONG).show()
        }

        if (arguments?.getInt("mapId")==1){
            val favouritesFragment= FavouritesFragment()
            val bundle=Bundle()
            bundle.putDouble("lat",lat)
            bundle.putDouble("lon",lon)
            bundle.putInt("id",1)
            favouritesFragment.arguments=bundle
            findNavController().navigate(R.id.action_mapsFragment_to_favoriteFragment,bundle)
        }else{
            prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString("lat", (lat.toString()))
            editor.putString("lon", (lon.toString()))
            editor.apply()
            editor.commit()
            findNavController().navigate(
                    R.id.action_Fragment_to_home_Fragment
            )
        }


    }
}