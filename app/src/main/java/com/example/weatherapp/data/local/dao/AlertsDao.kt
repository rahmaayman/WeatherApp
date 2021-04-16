package com.example.weatherapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.data.local.entities.AlertsEntity

@Dao
interface AlertsDao {

    @Query("SELECT * FROM alert_table")
    fun getAllAlarms(): LiveData<List<AlertsEntity>>

    @Query("SELECT * FROM alert_table Where id = :id ")
    fun getApiObj(id:Int): AlertsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarmObj: AlertsEntity):Long

    @Query("DELETE FROM alert_table WHERE id = :id")
    suspend fun deleteAlarmObjById(id:Int)
}