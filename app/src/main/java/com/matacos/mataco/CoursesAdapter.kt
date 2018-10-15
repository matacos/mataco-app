package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.Course
import org.json.JSONObject

class CoursesAdapter(val context: Context, val coursesList: ArrayList<Course>, val preferences: SharedPreferences): RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder>() {

    private val TAG: String = CoursesAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.number.text = coursesList[position].number()
        holder.total_slots.text = coursesList[position].totalSlots()
        holder.professors.text = coursesList[position].professors()
        holder.classroomCampus.text = coursesList[position].classroomCampus()
        val enroled = preferences.getBoolean("subject_enroled", false)

        holder.button_sign_up.setOnClickListener {
            Log.d(TAG, "onClick: clicked on button_sign_up")

            postData(coursesList[position].number)
            notifyDataSetChanged()
            val intent = Intent(context, SubjectsActivity::class.java)
            context.startActivity(intent)
        }

        if (enroled) {
            holder.button_sign_up.setEnabled(false)
        }else {
            if (coursesList[position].totalSlots.toInt() <= 0){
                holder.button_sign_up.setText("Inscribirse como condicional")
            } else{
                holder.button_sign_up.setText("Inscribirse")
            }
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
        val button_sign_up = itemView.findViewById<Button>(R.id.button_sign_up)!!
        val parentLayout = itemView.findViewById<android.support.constraint.ConstraintLayout>(R.id.courses_parent_layout)!!

    }

    private fun postData(course: String){
        Log.d(TAG, "postData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val token = preferences.getString("token", "")
        val username = preferences.getString("username", "")
        val path = "api/cursadas"
        val params = JSONObject()
        params.put("student", username)
        params.put("course", course)
        Log.d(TAG, "Params: ${params}")

        apiController.post(path, token, params){ response ->
            Log.d(TAG, response.toString())
        }
    }
}