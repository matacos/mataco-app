package com.matacos.mataco

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*

class CoursesAdapter(val context: Context, val coursesList: ArrayList<Course>, val preferences: SharedPreferences): RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder>() {

    private val TAG: String = CoursesAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.number.text = "Cátedra ${coursesList[position].number}"
        holder.total_slots.text = "Cupos ${coursesList[position].totalSlots}"
        holder.professors.text = coursesList[position].professors
        holder.classroomCampus.text = "Sede ${coursesList[position].classroomCampus}"
        holder.button.setOnClickListener {
            Log.d(TAG, "onClick: clicked on button")

            Toast.makeText(context, "Clicked Button", Toast.LENGTH_SHORT).show()

        }

        holder.time_slots_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        holder.time_slots_recycler_view.adapter = TimeSlotsAdapter(context, coursesList[position].timeSlots)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.courses_item_layout, parent, false)
        return CoursesViewHolder(v)
    }



    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return coursesList.size
    }

    class CoursesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val number = itemView.findViewById<TextView>(R.id.number)!!
        val total_slots = itemView.findViewById<TextView>(R.id.total_slots)!!
        val professors = itemView.findViewById<TextView>(R.id.professors)!!
        val classroomCampus = itemView.findViewById<TextView>(R.id.classroom_campus)!!
        val time_slots_recycler_view = itemView.findViewById<RecyclerView>(R.id.time_slots_recycler_view)!!
        val button = itemView.findViewById<Button>(R.id.button)!!
        val parentLayout = itemView.findViewById<android.support.constraint.ConstraintLayout>(R.id.courses_parent_layout)!!

    }

}