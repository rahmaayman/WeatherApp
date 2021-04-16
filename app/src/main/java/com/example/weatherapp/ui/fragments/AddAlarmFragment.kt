package com.example.weatherapp.ui.fragments

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entities.AlertsEntity
import com.example.weatherapp.databinding.FragmentAddAlarmBinding
import com.example.weatherapp.viewModels.AlertsViewModel
import com.example.weatherapp.util.AlarmReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AddAlarmFragment : Fragment() {
    lateinit var binding:FragmentAddAlarmBinding
    lateinit var alertsEntity: AlertsEntity
    var calStart = Calendar.getInstance()
    var calEnd = Calendar.getInstance()
    lateinit var alertsViewModel: AlertsViewModel

  /*  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextUtils.setLocale(requireActivity(), Setting.lang)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddAlarmBinding.inflate(inflater,container,false)
        var cal=Calendar.getInstance()
        alertsEntity= AlertsEntity("","","","","",true)
        alertsViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(
                AlertsViewModel::class.java)
        binding.txtTimeFrom.setOnClickListener { v ->

            val hour = cal.get(Calendar.HOUR)
            val minute = cal.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                calStart.set(Calendar.HOUR_OF_DAY,h)
                calStart.set(Calendar.MINUTE,m)
                calStart.set(Calendar.SECOND,0)

                val format = SimpleDateFormat("hh:mm aaa")
                alertsEntity.start = format.format(calStart.time)
                //binding.fromTimeImg.setText(format.format(calStart.time))

//                alarmObj.start= "$h : $m"
                binding.txtTimeFrom.text="$h : $m"

                Toast.makeText(requireContext(), h.toString() + " : " + m +" : " , Toast.LENGTH_LONG).show()

            }),hour,minute,false)

            tpd.show()
        }
        binding.txtTimeTo.setOnClickListener { v ->

            val hour = cal.get(Calendar.HOUR)
            val minute = cal.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                calEnd.set(Calendar.HOUR_OF_DAY,h)
                calEnd.set(Calendar.MINUTE,m)
                calEnd.set(Calendar.SECOND,0)

                val format = SimpleDateFormat("hh:mm aaa")
                alertsEntity.end = format.format(calEnd.time)
                //binding.fromTimeImg.setText(format.format(calStart.time))

//                alarmObj.start= "$h : $m"
                binding.txtTimeTo.text="$h : $m"

                Toast.makeText(requireActivity().applicationContext, h.toString() + " : " + m +" : " , Toast.LENGTH_LONG).show()

            }),hour,minute,false)

            tpd.show()
        }
        //binding.txtEvent.text=getEvent()
        binding.alertDate.setOnClickListener { v ->
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calStart.set(Calendar.YEAR, year)
                calStart.set(Calendar.MONTH, monthOfYear)
                calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                calEnd.set(Calendar.YEAR, year)
                calEnd.set(Calendar.MONTH, monthOfYear)
                calEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                Log.i("alarm", "" + calStart)
                Log.i("alarm", "" + calEnd.timeInMillis)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                alertsEntity.date = sdf.format(calStart.time)
                binding.alertDate.text = sdf.format(calStart.time)

            }
            var datePickerDialog=DatePickerDialog(requireContext(), dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.addAlarmBtn.setOnClickListener {
            if(calStart.timeInMillis<calEnd.timeInMillis){
                alertsEntity.description=binding.edtDescription.text.toString()
                alertsEntity.event=getEvent()
                if (binding.alarm.isChecked){
                    alertsEntity.status=false
                }else{
                    alertsEntity.status=true
                }

               // Toast.makeText(requireActivity().applicationContext,alertsEntity.toString(),Toast.LENGTH_LONG).show()
                var id=0
                var jop= CoroutineScope(Dispatchers.IO).launch {
                    id= alertsViewModel.insertAlarm(alertsEntity).toInt()
                }
                jop.invokeOnCompletion {
                    setAlarm(requireActivity().applicationContext, id, calStart, calEnd, alertsEntity.event,alertsEntity.status)
                }

               // Toast.makeText(requireActivity(), "${arguments?.getInt("mapId")} ", Toast.LENGTH_LONG).show()
                Navigation.findNavController(it).navigate(R.id.action_add_alert_fragment_to_alert_fragment)
            }
        }

        return binding.root
    }

    private fun setAlarm(context: Context, id: Int, calStart: Calendar, calEnd: Calendar, event: String, status: Boolean) {
        Log.i("alarm","in setAlarm method")
        val mIntent=Intent(context,AlarmReceiver::class.java)
        mIntent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        mIntent.putExtra("endTime",calEnd.timeInMillis)
        Log.i("alarm","in setAlarm method endtime ${calEnd.timeInMillis}")
        mIntent.putExtra("id",id)
        Log.i("alarm","in setAlarm method id $id")
        mIntent.putExtra("event",event)
        Log.i("alarm","in setAlarm method event $event")
        mIntent.putExtra("status",status)
        Log.i("alarm","in setAlarm method status $status")
        val mPendingIntent=PendingIntent.getBroadcast(context,id,mIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val mAlarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.i("alarm","in setAlarm method,alarm servuse")
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calStart.timeInMillis,2*1000,mPendingIntent)
    }

    private fun getEvent(): String {
        var event = ""
        var arr = this.resources.getStringArray(R.array.event_options)
        when (binding.spinnerEvent.getSelectedItemPosition()) {
            0 -> event = arr[0]
            1 -> event = arr[1]
            2 -> event = arr[2]
            3 -> event = arr[3]
            4 -> event = arr[4]
            5 -> event = arr[5]
        }
        return event
    }


}