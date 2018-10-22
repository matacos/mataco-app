package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.messaging.FirebaseMessaging
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.Exam
import org.json.JSONObject

class ExamsAdapter(val context: Context, val examsList: ArrayList<Exam>, val preferences: SharedPreferences) : RecyclerView.Adapter<ExamsAdapter.ExamsViewHolder>(), AdapterView.OnItemSelectedListener {

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

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter(context, android.R.layout.simple_spinner_item, statusList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner.adapter = adapter
        holder.spinner.onItemSelectedListener = this
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


    class ExamsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exam_title: TextView = itemView.findViewById(R.id.exam_title)!!
        val classroomCode: TextView = itemView.findViewById(R.id.classroom_code)!!
        val beginning: TextView = itemView.findViewById(R.id.beginning)!!
        val ending: TextView = itemView.findViewById(R.id.ending)!!
        val classroomCampus: TextView = itemView.findViewById(R.id.classroom_campus)!!
        val buttonSignUp: Button = itemView.findViewById(R.id.button_sign_up)!!
        val spinner: Spinner = itemView.findViewById(R.id.spinner)!!
    }

    private fun postData(id: Int) {
        Log.d(TAG, "postData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val token = preferences.getString("token", "")
        val username = preferences.getString("username", "")
        val path = "api/inscripciones_final"
        val params = JSONObject()
        params.put("student", username)
        params.put("exam_id", id)
        params.put("enrolment_type", status)
        Log.d(TAG, "Params: $params")

        apiController.post(path, token, params) { response ->
            Log.d(TAG, response.toString())
            val intent = Intent(context, ExamSubjectsActivity::class.java)
            context.startActivity(intent)
        }
    }
}