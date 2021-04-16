package com.example.weatherapp.util

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.example.weatherapp.repo.WeatherRepository
import com.example.weatherforcast.data.roomdb.LocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AlarmReceiver: BroadcastReceiver() {
    lateinit var prefs: SharedPreferences
    lateinit var notificationUtils: NotificationUtils
    var notificationManager: NotificationManager?=null
    override fun onReceive(context: Context, intent: Intent) {
        notificationUtils= NotificationUtils(context)
        notificationManager=notificationUtils.getNManager()
        val c=Calendar.getInstance()
        val id=intent.getIntExtra("id",0)
        val event=intent.getStringExtra("event")
        val status=intent.getBooleanExtra("status",true)
        val longEndTime=intent.getLongExtra("endTime",0)
        if (longEndTime<c.timeInMillis){
            Log.i("alarm","time is small")
            cancleAlarm(id,context)
            CoroutineScope(Dispatchers.IO).launch {
                val weatherRepository=WeatherRepository(context.applicationContext as Application)
                weatherRepository.deleteAlarmObj(id)
                Log.i("alarm", " cancel")
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Alarm canceled ", Toast.LENGTH_SHORT).show()
                }

            }
        }else{
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val timeZone = prefs.getString("timezone", "").toString()
            CoroutineScope(Dispatchers.IO).launch {
                val weatherRepository=WeatherRepository(context.applicationContext as Application)
                val weatherResponse=weatherRepository.getApiObj(timeZone)
                if (weatherResponse.alerts==null){
                    Log.i("alarm",""+event)
                    Log.i("alarm","no alerts")
                    notifyUser(context,event+"","there is no alerts",id,status)
                }
                Log.i("alarm",""+event)
                Log.i("alarm",""+weatherResponse.current.weather.get(0).description)
                if (weatherResponse.current.weather[0].description.contains(event + "",ignoreCase = true)){
                    notifyUser(context,event+"",weatherResponse.current.weather.get(0).description,id,status)
                }
            }
        }
    }

    private fun notifyUser(context: Context, event: String, description: String, id: Int, status: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val nb:NotificationCompat.Builder?=notificationUtils.getAndroidChannelNotification("Event "+event,description,status)
            val notification=nb?.build()
            if (!status){
                notification?.flags= NotificationCompat.FLAG_INSISTENT
            }
            notificationUtils.getNManager()?.notify(id,notification)
        }

    }

    fun cancleAlarm(id: Int, context: Context) {
        notificationManager?.cancel(id)
        Log.i("alarmID", "" + id)
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}