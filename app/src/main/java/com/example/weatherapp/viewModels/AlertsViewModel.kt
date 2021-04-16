package com.example.weatherapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weatherapp.data.local.entities.AlertsEntity
import com.example.weatherapp.repo.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsViewModel(application: Application): AndroidViewModel(application) {

    var weatherRepository:WeatherRepository
    init {
        weatherRepository= WeatherRepository(application)
    }
    fun deleteAlarmObjectById(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.deleteAlarmObj(id)
        }
    }
    suspend fun insertAlarm(alertsEntity: AlertsEntity):Long{
        return weatherRepository.insertAlarm(alertsEntity)
    }
    fun getAlertsList():LiveData<List<AlertsEntity>>{
        return weatherRepository.getAllAlarmObj()
    }

}