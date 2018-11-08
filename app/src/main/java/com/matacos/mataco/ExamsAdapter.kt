package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.Exam
import org.json.JSONObject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ExamsAdapter(val context: Context, val examsList: ArrayList<Exam>, val preferences: SharedPreferences) : androidx.recyclerview.widget.RecyclerView.Adapter<ExamsAdapter.ExamsViewHolder>(), AdapterView.OnItemSelectedListener {

    private val TAG: String = ExamsAdapter::class.java.simpleName
    var statusList: Array<String> = arrayOf("Regular", "Libre")
    var status: String = "Regular"

    override fun onBindViewHolder(holder: ExamsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.exam_title.text = examsList[position].title()
        holder.classroomCode.text = examsList[position].classroomCode
        holder.beginning.text = examsList[position].beginning()
        holder.ending.text = examsList[position].ending()
        holder.classroomCampus.text = examsList[position].classroomCampus()

        val approvedCourse: Boolean = preferences.getBoolean("subject_approved_course", false)
        if (approvedCourse) {
            holder.condition.text = "Regular"
        } else {
            holder.condition.text = "Libre"
        }
        holder.buttonSignUp.setOnClickListener {
            Log.d(TAG, "onClick: clicked on button_sign_up")

            postData(examsList[position].id)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        status = statusList[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamsViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.exam_item_layout, parent, false)
        return ExamsViewHolder(v)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return examsList.size
    }


    class ExamsViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val exam_title: TextView = itemView.findViewById(R.id.exam_title)!!
        val classroomCode: TextView = itemView.findViewById(R.id.classroom_code)!!
        val beginning: TextView = itemView.findViewById(R.id.beginning)!!
        val ending: TextView = itemView.findViewById(R.id.ending)!!
        val classroomCampus: TextView = itemView.findViewById(R.id.classroom_campus)!!
        val buttonSignUp: Button = itemView.findViewById(R.id.button_sign_up)!!
        val condition: Button = itemView.findViewById(R.id.condition)!!
    }

    private fun postData(id: Int) {
        Log.d(TAG, "postData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val token: String = preferences.getString("token", "")
        val username: String = preferences.getString("username", "")
        val path = "api/inscripciones_final"
        val params = JSONObject()
        params.put("student", username)
        params.put("exam_id", id)
        params.put("enrolment_type", status)
        Log.d(TAG, "Params: $params")

        apiController.post(path, token, params) { response ->
            Log.d(TAG, response.toString())
            val intent = Intent(context, ExamSubjectsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}