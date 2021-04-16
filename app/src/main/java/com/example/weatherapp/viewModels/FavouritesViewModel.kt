package com.example.weatherapp.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.repo.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesViewModel (application: Application): AndroidViewModel(application) {
    val weatherResponse=MutableLiveData<WeatherResponse>()
    lateinit var weatherRepository:WeatherRepository
    init {
        weatherRepository= WeatherRepository(application)
    }
    fun loadWeather(context: Context,lat:Double,lon:Double,lang:String,units:String):LiveData<List<WeatherResponse>>{
        return weatherRepository.fetchWeatherData(context,lat,lon,lang,units)
    }
    fun getWeatherDataFromRoom():LiveData<List<WeatherResponse>>{
        return weatherRepository.getWeatherDataFromRoom()
    }

    fun deleteWeatherObjectByTimeZone(timeZone:String){
        CoroutineScope(Dispatchers.IO).launch{
            weatherRepository.deleteApiObj(timeZone)
        }
    }


}