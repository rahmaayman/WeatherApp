package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemHourlyBinding
import com.example.weatherapp.model.Hourly
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HourlyAdapter (var hourlyList: ArrayList<Hourly>):RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    class HourlyViewHolder(var view:ItemHourlyBinding):RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding=ItemHourlyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HourlyViewHolder(binding)
    }
    fun changeData(newList: List<Hourly>){
        hourlyList.clear()
        hourlyList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int =hourlyList.size

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val item=hourlyList[position]
        holder.view.txtTemp.text=item.temp.toInt().toString()+"°"
        holder.view.txtDesc.text=item.weather[0].description
        holder.view.txtHour.text=timeFormat(item.dt.toInt())
        Glide.with(holder.view.imgIcon.context)
            .load(item.weather[0].icon?.let { getImage(it) })
            .placeholder(R.drawable.ic_cloudya)
            .into(holder.view.imgIcon)
    }
    private fun timeFormat(millisSeconds:Int ): String {
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())
        val format = SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)
    }
    fun getImage(icon: String): String {
        return "http://openweathermap.org/img/w/${icon}.png"
    }
}