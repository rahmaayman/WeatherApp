package com.example.weatherapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemDailyBinding
import com.example.weatherapp.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(var dailyList: ArrayList<Daily>, context: Context) : RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {
    lateinit var context: Context
    init {
        this.context=context
    }
    class DailyViewHolder(var view: ItemDailyBinding):RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding= ItemDailyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DailyViewHolder(binding)
    }

    override fun getItemCount(): Int =dailyList.size

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val item=dailyList[position]
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(dailyList[position].dt.toLong()*1000)
        Log.i("day",calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()))
        holder.view.txtDay.text = localizingDays(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),context)
        //holder.myView.daydate.text = date
        holder.view.txtDesc.text=item.weather[0].description
        holder.view.txtMaxTemp.text=item.temp.max.toInt().toString()+"°"
        holder.view.txtMinTemp.text=item.temp.min.toInt().toString()+"°"
        Glide.with(holder.view.imgIcon.context)
            .load(item.weather[0].icon?.let { getImage(it) })
            .placeholder(R.drawable.ic_cloudya)
            .into(holder.view.imgIcon)
    }
    fun changeData(newList: List<Daily>){
        dailyList.clear()
        dailyList.addAll(newList)
        notifyDataSetChanged()
    }
    private fun timeFormat(millisSeconds:Int ): String {
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())
        val format = SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)
    }
    private fun getImage(icon: String): String {
        return "http://openweathermap.org/img/w/${icon}.png"
    }
    private fun localizingDays(day:String,context: Context):String{

        return when (day.trim()) {
            "Saturday" ->context.getString(R.string.saturday)
            "Sunday" ->context.getString(R.string.sunday)
            "Monday" ->context.getString(R.string.monday)
            "Tuesday" ->context.getString(R.string.tuesday)
            "Wednesday" ->context.getString(R.string.wednesday)
            "Friday" ->context.getString(R.string.friday)
            "Thursday" ->context.getString(R.string.thursday)
            else ->day
        }

    }
}