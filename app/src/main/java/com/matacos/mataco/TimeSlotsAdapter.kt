package com.matacos.mataco

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*
import com.matacos.mataco.clases.TimeSlot

class TimeSlotsAdapter(val context: Context, val timeSlotsList: List<TimeSlot>) : androidx.recyclerview.widget.RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotsViewHolder>() {

    private val TAG: String = TimeSlotsAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: TimeSlotsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.classroom_code.text = timeSlotsList[position].classroomCode
        holder.description.text = timeSlotsList[position].description
        holder.day_of_week.text = timeSlotsList[position].dayOfWeek
        holder.beginning.text = timeSlotsList[position].beginning()
        holder.ending.text = timeSlotsList[position].ending()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotsViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.time_slots_item_layout, parent, false)
        return TimeSlotsViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return timeSlotsList.size
    }

    class TimeSlotsViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val classroom_code: TextView = itemView.findViewById(R.id.classroom_code)!!
        val description: TextView = itemView.findViewById(R.id.description)!!
        val day_of_week: TextView = itemView.findViewById(R.id.day_of_week)!!
        val beginning: TextView = itemView.findViewById(R.id.beginning)!!
        val ending: TextView = itemView.findViewById(R.id.ending)!!

    }

}