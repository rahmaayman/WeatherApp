package com.example.weatherapp.adapters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.local.entities.AlertsEntity
import com.example.weatherapp.databinding.ItemAlarmBinding
import com.example.weatherapp.viewModels.AlertsViewModel
import com.example.weatherapp.util.AlarmReceiver
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.ArrayList

class AlertsAdapter(var alarmList: ArrayList<AlertsEntity>, alartViewModel: AlertsViewModel, context: Context) : RecyclerView.Adapter<AlertsAdapter.AlertsViewHolder>(){
    lateinit var context: Context
    lateinit var alartViewModel: AlertsViewModel
    lateinit var removedAlarmObj:AlertsEntity
    private var removedposition=0
    init {
        this.context=context
        this.alartViewModel=alartViewModel
    }
    class AlertsViewHolder(var myView:ItemAlarmBinding):RecyclerView.ViewHolder(myView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertsViewHolder {
        val binding=ItemAlarmBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AlertsViewHolder(binding)
    }

    override fun getItemCount(): Int =alarmList.size

    override fun onBindViewHolder(holder: AlertsViewHolder, position: Int) {
        holder.myView.txtEventType.text=alarmList[position].event
        holder.myView.txtAlarmDate.text = ""+alarmList[position].start+" to "+alarmList[position].end+" "+alarmList[position].date
        holder.myView.txtAlarmDetails.text =alarmList[position].description

    }
    fun changeData(newList :List<AlertsEntity>){
        alarmList.clear()
        alarmList.addAll(newList)
        notifyDataSetChanged()
    }
    fun removeFromAdapter(viewHolder: RecyclerView.ViewHolder){
        removedposition=viewHolder.adapterPosition
        removedAlarmObj=alarmList[viewHolder.adapterPosition]
        alarmList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
        Snackbar.make(viewHolder.itemView, "${removedAlarmObj.event} removed", Snackbar.LENGTH_LONG).apply {
            setAction("UNDO") {
                alarmList.add(removedposition, removedAlarmObj)
                notifyItemInserted(removedposition)
            }
            addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
                override fun onShown(transientBottomBar: Snackbar?) {
                    super.onShown(transientBottomBar)
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (event== Snackbar.Callback.DISMISS_EVENT_TIMEOUT){
                        removeForever(removedAlarmObj.id)
                    }
                }
            })
        }.show()
    }

    private fun removeForever(id: Int) {
        alartViewModel.deleteAlarmObjectById(id)
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,id, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}