package com.example.weatherapp.util

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.repo.WeatherRepository

class MyWorkerApi(appContext: Context, workerParams: WorkerParameters) : Worker(appContext,workerParams){
    var weatherRepository=WeatherRepository(appContext.applicationContext as Application)
    var prefs= PreferenceManager.getDefaultSharedPreferences(applicationContext)
    override fun doWork(): Result {
        var unit=prefs.getString("UNIT_SYSTEM","metric").toString()
        var lang=prefs.getString("LANGUAGE_SYSTEM", "en").toString()
        weatherRepository.UpdateWeatherData(lang,unit,applicationContext)
        return Result.success()
    }
}