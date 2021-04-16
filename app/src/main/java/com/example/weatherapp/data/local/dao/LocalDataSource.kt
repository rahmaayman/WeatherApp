package com.example.weatherforcast.data.roomdb

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weatherapp.data.local.dao.AlertsDao
import com.example.weatherapp.data.local.entities.AlertsEntity
import com.example.weatherapp.model.WeatherResponse
class LocalDataSource {
    lateinit var apiObjDao: WeatherResponseDao
    lateinit var alertsDao: AlertsDao
    constructor (application: Application) {
        apiObjDao = ApiObjDataBase.getDatabase(application).apiObjDao()
        alertsDao=ApiObjDataBase.getDatabase(application).alertObjDao()
    }

    fun getAll(): LiveData<List<WeatherResponse>> {
        return apiObjDao.getAllWeatherResponse()
    }

    fun getAllList(): List<WeatherResponse> {
        return apiObjDao.getAllList()
    }

    suspend fun insert(apiObj: WeatherResponse) {
        apiObjDao.insert(apiObj)
    }



     suspend fun deleteApiObj(timeZone: String) {
        apiObjDao.deleteApiObj(timeZone)
    }

     fun getApiObj(timeZone:String) :WeatherResponse {
        return apiObjDao.getApiObj(timeZone)
    }
    suspend fun deleteAlarmObj(id: Int) {
        alertsDao.deleteAlarmObjById(id)
    }

    fun getAllAlarmObj(): LiveData<List<AlertsEntity>> {
        return alertsDao.getAllAlarms()
    }

    suspend fun insertAlarm(alertsEntity: AlertsEntity):Long {
        return alertsDao.insertAlarm(alertsEntity)
    }



}