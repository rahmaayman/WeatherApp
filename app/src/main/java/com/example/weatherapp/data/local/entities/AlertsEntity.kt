package com.example.weatherapp.data.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertsEntity(
        var event:String,
        var date:String,
        var start:String,
        var end:String,
        var description:String,
        var status:Boolean

){
    @PrimaryKey(autoGenerate = true) var id:Int=0
}