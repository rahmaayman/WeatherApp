package com.example.weatherforcast.data.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.model.WeatherResponse


@Dao
interface WeatherResponseDao {

    @Query("SELECT * FROM WeatherResponse")
    fun getAllWeatherResponse(): LiveData<List<WeatherResponse>>

    @Query("SELECT * FROM WeatherResponse")
    fun getAllList(): List<WeatherResponse>

    @Query("SELECT * FROM WeatherResponse Where timezone = :timezone ")
    fun getApiObj(timezone:String): WeatherResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apiObj: WeatherResponse)

    @Query("DELETE FROM WeatherResponse WHERE timezone = :timezone")
        suspend fun deleteApiObj(timezone:String)
}