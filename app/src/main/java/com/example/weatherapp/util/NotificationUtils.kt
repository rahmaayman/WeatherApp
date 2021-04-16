package com.example.weatherapp.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.weatherapp.R

class NotificationUtils(base: Context?) : ContextWrapper(base) {
    var notificationManager: NotificationManager? = null
    val ANDROID_CHANNEL_ID="COM.ANDROID.CHANNEL.ID.NOTIFICATION.MANAGER"
    val ANDROID_CHANNEL_NAME="ANDROID_CHANNEL_NAME"
    init {
        createChannel()
    }

    private fun createChannel() {
        var androidChannel:NotificationChannel?=null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            androidChannel= NotificationChannel(
                    ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            )
            androidChannel.enableLights(true)
            androidChannel.enableVibration(true)
            androidChannel.lightColor=Color.BLUE
            androidChannel.lockscreenVisibility=Notification.VISIBILITY_PRIVATE
            getNManager()?.createNotificationChannel(androidChannel)
        }
    }
    fun getNManager():NotificationManager?{
        if (notificationManager==null){
            notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager
    }
    fun getAndroidChannelNotification( title: String,body: String,status:Boolean): NotificationCompat.Builder?{
        if (status){
            return NotificationCompat.Builder(applicationContext,ANDROID_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setOngoing(false)
        }else{
            return NotificationCompat.Builder(applicationContext,ANDROID_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setOngoing(false)
        }
    }
}