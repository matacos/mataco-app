package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley

class MyCoursesAdapter(val context: Context, val coursesList: ArrayList<Course>, val preferences: SharedPreferences): RecyclerView.Adapter<MyCoursesAdapter.MyCoursesViewHolder>() {

    private val TAG: String = MyCoursesAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: MyCoursesViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.code.text = coursesList[position].department_code + '.' + coursesList[position].subject_code
        holder.name.text = coursesList[position].subject_name
        holder.professors.text = "Cátedra ${coursesList[position].number} - ${coursesList[position].professors}"
        holder.classroomCampus.text = "Sede ${coursesList[position].classroomCampus}"
        holder.status.text = "Estado: " + if(coursesList[position].accepted) "Regular" else "Condicional"
        holder.button_drop_out.setOnClickListener {
            Log.d(TAG, "onClick: clicked on button_sign_up")

            deleteData(coursesList[position].number)
            notifyDataSetChanged()
            val intent = Intent(context, MyCoursesActivity::class.java)
            context.startActivity(intent)
        }

        holder.time_slots_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        holder.time_slots_recycler_view.adapter = TimeSlotsAdapter(context, coursesList[position].timeSlots)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCoursesViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_courses_item_layout, parent, false)
        return MyCoursesViewHolder(v)
    }



    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return coursesList.size
    }

    class MyCoursesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)!!
        val code = itemView.findViewById<TextView>(R.id.department_code)!!
        val professors = itemView.findViewById<TextView>(R.id.professors)!!
        val classroomCampus = itemView.findViewById<TextView>(R.id.classroom_campus)!!
        val time_slots_recycler_view = itemView.findViewById<RecyclerView>(R.id.time_slots_recycler_view)!!
        val button_drop_out = itemView.findViewById<Button>(R.id.button_drop_out)
        val status = itemView.findViewById<TextView>(R.id.status)!!
        val parentLayout = itemView.findViewById<android.support.constraint.ConstraintLayout>(R.id.courses_parent_layout)!!
    }

    private fun deleteData(course: String){
        Log.d(TAG, "deleteData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val token = preferences.getString("token", "")
        val username = preferences.getString("username", "")
        val path = "api/cursadas/$course-$username"

        apiController.delete(path, token){ response ->
            Log.d(TAG, response.toString())
        }
    }
}