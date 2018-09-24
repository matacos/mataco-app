package com.matacos.mataco

import android.content.Context
import android.content.SharedPreferences
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
        holder.name.text = coursesList[position].department + "." + coursesList[position].code + " " +coursesList[position].name
        holder.total_slots.text = coursesList[position].totalSlots
        holder.professors.text = coursesList[position].professors
        holder.button.setOnClickListener {
            Log.d(TAG, "onClick: clicked on: " + coursesList[position].name)

            Toast.makeText(context, "Clicked Button", Toast.LENGTH_SHORT).show()

        }
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
        val name = itemView.findViewById<TextView>(R.id.name)!!
        val total_slots = itemView.findViewById<TextView>(R.id.total_slots)!!
        val professors = itemView.findViewById<TextView>(R.id.professors)!!
        val button = itemView.findViewById<Button>(R.id.button)!!
        val parentLayout = itemView.findViewById<android.support.constraint.ConstraintLayout>(R.id.courses_parent_layout)!!

    }

}