package com.example.weatherapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.adapters.DailyAdapter
import com.example.weatherapp.adapters.HourlyAdapter
import com.example.weatherapp.databinding.FragmentFavouriteDetailsBinding
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.viewModels.HomeViewModel
import com.example.weatherapp.util.ContextUtils.Companion.setLocale
import com.example.weatherapp.util.ContextUtils.Companion.settings
import com.example.weatherapp.util.Setting
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouriteDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteDetailsBinding
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentFavouriteDetailsBinding.inflate(inflater,container,false)
        homeViewModel= ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(HomeViewModel::class.java)
        hourlyAdapter=HourlyAdapter(arrayListOf())
        dailyAdapter= DailyAdapter(arrayListOf(),requireActivity().applicationContext )
        settings(requireContext())
        initRecyclers()
        initUI();
        return binding.root
    }

    private fun initUI() {
       val str= arguments?.getString("weatherString")
        updateUI(Gson().fromJson(str, WeatherResponse::class.java))
    }

    private fun initRecyclers() {
        binding.rv24Hour.apply {
            adapter=hourlyAdapter
        }
        binding.rv7Day.apply {
            adapter=dailyAdapter
        }
    }
    private fun updateUI(item: WeatherResponse) {
        item?.let {
            setLocale(requireActivity(), Setting.lang)
            item.apply {
                binding.txtCity.text=it.timezone
                binding.txtDate.text="${homeViewModel.dateFormat(it.current.dt)} ${homeViewModel.timeFormat(it.current.dt)}"
                binding.txtVwTemp.text=it.current.temp.toInt().toString()+"°"
                CoroutineScope(Dispatchers.Main).launch{
                    Glide.with(binding.imgWeatherIcon).
                    load(hourlyAdapter.getImage(item.current.weather.get(0).icon)).
                    placeholder(R.drawable.ic_cloudya).into(binding.imgWeatherIcon)
                }
                binding.txtVwTempFeels.text=it.current.feels_like.toString()
                binding.txtVwDesc.text=it.current.weather[0].description
                binding.txtVwValueHumidity.text=it.current.humidity.toString()
                binding.txtVwValuePressure.text=it.current.pressure.toString()
                binding.txtVwValueSpeed.text=it.current.wind_speed.toString()
                binding.txtVwValueCloud.text=it.current.clouds.toString()
                dailyAdapter?.changeData(it.daily)
                hourlyAdapter.changeData(it.hourly)

            }
        }
        /*val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("timezone", item.timezone)
        editor.commit()*/
    }


}