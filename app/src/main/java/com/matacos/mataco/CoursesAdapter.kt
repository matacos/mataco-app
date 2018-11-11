package com.matacos.mataco

import android.annotation.SuppressLint
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
import org.json.JSONObject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CoursesAdapter(val context: Context, val coursesList: ArrayList<Course>, val preferences: SharedPreferences) : androidx.recyclerview.widget.RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder>() {

    private val TAG: String = CoursesAdapter::class.java.simpleName

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.number.text = coursesList[position].number()
        holder.totalSlots.text = coursesList[position].totalSlots()
        holder.professors.text = coursesList[position].professors()
        holder.classroomCampus.text = coursesList[position].classroomCampus()
        val enroled: Boolean = preferences.getBoolean("subject_enroled", false)
        val signUpCourses: Boolean = preferences.getBoolean("sign_up_courses", false)

        holder.buttonSignUp.setOnClickListener {
            Log.d(TAG, "onClick: clicked on button_sign_up")

            postData(coursesList[position].number)
        }

        if (enroled || !signUpCourses) {
            holder.buttonSignUp.isEnabled = false
        } else {
            holder.buttonSignUp.isEnabled = true
            if (coursesList[position].totalSlots.toInt() <= 0) {
                holder.buttonSignUp.text = "Inscribirse como condicional"
            } else {
                holder.buttonSignUp.text = "Inscribirse"
            }
        }

        holder.timeSlotsRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        holder.timeSlotsRecyclerView.adapter = TimeSlotsAdapter(context, coursesList[position].timeSlots)
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

    class CoursesViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val number: TextView = itemView.findViewById(R.id.number)!!
        val totalSlots: TextView = itemView.findViewById(R.id.total_slots)!!
        val professors: TextView = itemView.findViewById(R.id.professors)!!
        val classroomCampus: TextView = itemView.findViewById(R.id.classroom_campus)!!
        val timeSlotsRecyclerView: androidx.recyclerview.widget.RecyclerView = itemView.findViewById(R.id.time_slots_recycler_view)!!
        val buttonSignUp: Button = itemView.findViewById(R.id.button_sign_up)!!

    }

    private fun postData(course: String) {
        Log.d(TAG, "postData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val token: String = preferences.getString("token", "")
        val username: String = preferences.getString("username", "")
        val path = "api/cursadas"
        val params = JSONObject()
        params.put("student", username)
        params.put("course", course)
        Log.d(TAG, "Params: $params")

        apiController.post(path, token, params) { response ->
            Log.d(TAG, response.toString())

            val intent = Intent(context, SubjectsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}