package com.example.weatherapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adapters.FavouriteAdapter
import com.example.weatherapp.databinding.FragmentFavouritesBinding
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.util.Setting
import com.google.gson.Gson
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.viewModels.FavouritesViewModel
import com.example.weatherapp.util.ContextUtils.Companion.setLocale
import com.example.weatherapp.util.ContextUtils.Companion.settings

class FavouritesFragment : Fragment(){

    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favoriteAdapter:FavouriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(requireActivity(),Setting.lang)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favouritesViewModel =
                ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).
                get(FavouritesViewModel::class.java)
        //val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        binding= FragmentFavouritesBinding.inflate(inflater,container,false)
        favoriteAdapter= FavouriteAdapter(arrayListOf(),favouritesViewModel,requireActivity().applicationContext)
        settings(requireContext())
        //val bundle=Bundle()
        if (arguments?.getInt("id")==1){
            val lat=arguments?.getDouble("lat",0.0)
            val lon=arguments?.getDouble("lon",0.0)
            setLocale(requireActivity(),Setting.lang)
            observeViewModel(favouritesViewModel,lat!!,lon!!,Setting.lang,Setting.units)
          //  Toast.makeText(requireActivity(),"${lat.toString()},${lon.toString()}",Toast.LENGTH_LONG).show()
        }else{
            setLocale(requireActivity(),Setting.lang)
            getWeatherDataFromRoom(favouritesViewModel)
        }
        binding.btnFab.setOnClickListener {
            val bundle= Bundle()
            bundle.putInt("mapId",1)
            val mapsFragment= MapsFragment()
            mapsFragment.arguments=bundle
           // fragmentManager.
            Toast.makeText(requireActivity(), "${arguments?.getInt("mapId")} ", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).navigate(R.id.action_favourite_fragment_to_map_fragment,bundle)
        }
        initUI()

        return binding.root
    }

    private fun initUI() {
        binding.rvFavourites.apply {
            adapter=favoriteAdapter
        }
        val itemTouchHelperCallback=object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                favoriteAdapter.removeFromAdapter(viewHolder)
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvFavourites)
        favoriteAdapter.onItemClickListener=object :FavouriteAdapter.OnItemClickListener{
            override fun onClick(pos: Int, weatherResponse: WeatherResponse) {
                val weatherString=Gson().toJson(weatherResponse)
                val bundle:Bundle=Bundle()
                bundle.putString("weatherString",weatherString)
                val favouriteDetailsFragment= FavouriteDetailsFragment()
                favouriteDetailsFragment.arguments=bundle
                Log.i("ww",weatherResponse.toString())
                findNavController().navigate(R.id.action_favourite_fragment_to_favourite_details_fragment,bundle)
            }

        }

    }



    private fun observeViewModel(favouritesViewModel: FavouritesViewModel, lat: Double, lon: Double, lang:String, unit:String) {
        favouritesViewModel.loadWeather(requireActivity().applicationContext, lat, lon, lang, unit).observe(viewLifecycleOwner,{
            favoriteAdapter.changeData(it)
        })

    }

    private fun getWeatherDataFromRoom(favouritesViewModel: FavouritesViewModel) {
        favouritesViewModel.getWeatherDataFromRoom().observe(viewLifecycleOwner,{
            favoriteAdapter.changeData(it)
        })

    }


}