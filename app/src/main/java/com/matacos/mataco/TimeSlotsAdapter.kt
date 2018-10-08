package com.matacos.mataco

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*
import com.matacos.mataco.clases.TimeSlot
import java.text.SimpleDateFormat
import java.util.*

class TimeSlotsAdapter(val context: Context, val timeSlotsList: ArrayList<TimeSlot>): RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotsViewHolder>() {

    private val TAG: String = TimeSlotsAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: TimeSlotsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.classroom_code.text = timeSlotsList[position].classroomCode
        holder.description.text = timeSlotsList[position].description
        holder.day_of_week.text = timeSlotsList[position].dayOfWeek
        holder.beginning.text = formatDate(timeSlotsList[position].beginning)
        holder.ending.text = formatDate(timeSlotsList[position].ending)
    }

    fun formatDate(date: String): String {
        Log.d(TAG, "formatDate")
        val realDate: Date = SimpleDateFormat("HH:mm:ss", Locale.US).parse(date)
        val format = SimpleDateFormat("HH:mm", Locale.US)
        return format.format(realDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotsViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.time_slots_item_layout, parent, false)
        return TimeSlotsViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return timeSlotsList.size
    }

    class TimeSlotsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val classroom_code = itemView.findViewById<TextView>(R.id.classroom_code)!!
        val description = itemView.findViewById<TextView>(R.id.description)!!
        val day_of_week = itemView.findViewById<TextView>(R.id.day_of_week)!!
        val beginning = itemView.findViewById<TextView>(R.id.beginning)!!
        val ending = itemView.findViewById<TextView>(R.id.ending)!!
        val parentLayout = itemView.findViewById<android.support.constraint.ConstraintLayout>(R.id.time_slots_parent_layout)!!

    }

}