package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.Course

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyCoursesAdapter(val context: Context, val coursesList: ArrayList<Course>, val preferences: SharedPreferences) : androidx.recyclerview.widget.RecyclerView.Adapter<MyCoursesAdapter.MyCoursesViewHolder>() {

    private val TAG: String = MyCoursesAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: MyCoursesViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.code.text = coursesList[position].subject()
        holder.name.text = coursesList[position].subject_name
        holder.professors.text = coursesList[position].professors()
        holder.classroomCampus.text = coursesList[position].classroomCampus()
        holder.status.text = coursesList[position].state()
        val dropOutCourses: Boolean = preferences.getBoolean("drop_out_courses", false)

        holder.buttonDropOut.setOnClickListener {
            Log.d(TAG, "onClick: clicked on button_drop_out")

            deleteData(coursesList[position].number, position)
        }

        if (!dropOutCourses) {
            holder.buttonDropOut.isEnabled = false
        } else {
            holder.buttonDropOut.isEnabled = true
        }

        holder.timeSlotsRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        holder.timeSlotsRecyclerView.adapter = TimeSlotsAdapter(context, coursesList[position].timeSlots)
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

    class MyCoursesViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)!!
        val code: TextView = itemView.findViewById(R.id.department_code)!!
        val professors: TextView = itemView.findViewById(R.id.professors)!!
        val classroomCampus: TextView = itemView.findViewById(R.id.classroom_campus)!!
        val timeSlotsRecyclerView: androidx.recyclerview.widget.RecyclerView = itemView.findViewById(R.id.time_slots_recycler_view)!!
        val buttonDropOut: Button = itemView.findViewById(R.id.button_drop_out)
        val status: TextView = itemView.findViewById(R.id.status)!!
    }

    private fun deleteData(course: String, position: Int) {
        Log.d(TAG, "deleteData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val token: String = preferences.getString("token", "")
        val username: String = preferences.getString("username", "")
        val path = "api/cursadas/$course-$username"

        apiController.delete(path, token) { response ->
            Log.d(TAG, response.toString())

            coursesList.removeAt(position)
            notifyDataSetChanged()
            if (coursesList.isEmpty()) {
                val intent = Intent(context, MyCoursesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}