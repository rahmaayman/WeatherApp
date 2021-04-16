package com.example.weatherapp.adapters

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ItemFavouriteBinding
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.viewModels.FavouritesViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class FavouriteAdapter(var favouriteList:ArrayList<WeatherResponse>, favouritesViewModel: FavouritesViewModel, context: Context):RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(var myView:ItemFavouriteBinding) : RecyclerView.ViewHolder(myView.root)
    private var removedPosition=0
    lateinit var removedObject:WeatherResponse
    private var favouriteViewModel= FavouritesViewModel(context.applicationContext as Application)
    private var context:Context
    init {
        this.favouriteViewModel=favouriteViewModel
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val binding=ItemFavouriteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavouriteViewHolder(binding)
    }

    override fun getItemCount(): Int = favouriteList.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val item=favouriteList[position]
        holder.myView.txtCity.text=item.timezone
        holder.myView.txtDesc.text=item.current.weather[0].description
        holder.myView.txtTemp.text="${item.current.temp.toInt().toString()}°";
        holder.myView.favItem.setOnClickListener {
            if (item!=null){
                onItemClickListener?.onClick(position,item);
            }
        }

    }
    fun changeData(newList :List<WeatherResponse>){
        favouriteList.clear()
        favouriteList.addAll(newList)
        notifyDataSetChanged()
    }
    fun removeFromAdapter(viewHolder: RecyclerView.ViewHolder){
        removedPosition=viewHolder.adapterPosition
        removedObject=favouriteList[viewHolder.adapterPosition]
        favouriteList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
        Snackbar.make(viewHolder.itemView,"${removedObject.timezone} removed",Snackbar.LENGTH_LONG).apply {
            setAction("Undo"){
                favouriteList.add(removedPosition,removedObject)
                notifyItemInserted(removedPosition)
            }
            addCallback(object :BaseTransientBottomBar.BaseCallback<Snackbar>(){
                override fun onShown(transientBottomBar: Snackbar?) {
                    super.onShown(transientBottomBar)
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (event==Snackbar.Callback.DISMISS_EVENT_TIMEOUT){
                        favouriteViewModel.deleteWeatherObjectByTimeZone(removedObject.timezone)
                    }
                }
            })
        }.show()
    }
    var onItemClickListener:OnItemClickListener?=null
    interface OnItemClickListener{
        fun onClick(pos:Int,weatherResponse: WeatherResponse)
    }


}