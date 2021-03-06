package com.example.weatherforcast.data.roomdb

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.local.dao.AlertsDao
import com.example.weatherapp.data.local.entities.AlertsEntity
import com.example.weatherapp.model.WeatherResponse

@Database(entities = [WeatherResponse::class, AlertsEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class ApiObjDataBase : RoomDatabase() {
    abstract fun apiObjDao(): WeatherResponseDao
    abstract fun alertObjDao():AlertsDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ApiObjDataBase? = null

        fun getDatabase(application: Application): ApiObjDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        application.applicationContext,
                        ApiObjDataBase::class.java,
                        "WeatherDB"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}