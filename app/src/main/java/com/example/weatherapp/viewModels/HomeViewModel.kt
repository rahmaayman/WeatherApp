package com.example.weatherapp.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.repo.WeatherRepository
import java.text.SimpleDateFormat
import java.util.*


class HomeViewModel (application: Application) : AndroidViewModel(application) {
        var weatherRepository:WeatherRepository

        init{
            weatherRepository = WeatherRepository(application)
        }

        fun getApiObjFromRoom(timeZone:String): WeatherResponse{
            return weatherRepository.getApiObjFromRoom(timeZone)
        }


        fun loadWeatherObj(context: Context, lat:Double, lon:Double, lang:String, unit:String) : LiveData<WeatherResponse> {
            weatherRepository.fetchWeatherObj(context,lat,lon,lang,unit)
            return weatherRepository.weatherObj
        }

        fun updateAllData(context: Context, lang: String, unit: String){
            weatherRepository.UpdateWeatherData(lang,unit,context)

        }

        fun dateFormat(milliSeconds: Int):String{
            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar: Calendar = Calendar.getInstance()
            calendar.setTimeInMillis(milliSeconds.toLong() * 1000)
            var month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            var day=calendar.get(Calendar.DAY_OF_MONTH).toString()
            var year=calendar.get(Calendar.YEAR).toString()
            return day+"/"+month// +"/"+year

        }

        fun timeFormat(millisSeconds:Int ): String {
            val calendar = Calendar.getInstance()
            calendar.setTimeInMillis((millisSeconds * 1000).toLong())
            val format = SimpleDateFormat("hh:00 aaa")
            return format.format(calendar.time)
        }


    }


