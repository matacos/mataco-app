package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.constraint.ConstraintLayout
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
import com.matacos.mataco.clases.Exam
import org.json.JSONObject

class ExamsAdapter(val context: Context, val examsList: ArrayList<Exam>, val preferences: SharedPreferences): RecyclerView.Adapter<ExamsAdapter.ExamsViewHolder>(), AdapterView.OnItemSelectedListener{

    private val TAG: String = ExamsAdapter::class.java.simpleName
    var statusList = arrayOf("Regular","Libre")
    var status:String = "Regular"

    override fun onBindViewHolder(holder: ExamsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.id.text = examsList[position].id()
        holder.classroom_code.text = examsList[position].classroomCode
        holder.date.text = examsList[position].date()
        holder.beginning.text = examsList[position].beginning()
        holder.ending.text = examsList[position].ending()
        holder.classroom_campus.text = examsList[position].classroomCampus()
        holder.examiner.text = examsList[position].examiner.toString()

        val adapter:ArrayAdapter<CharSequence> = ArrayAdapter(context, android.R.layout.simple_spinner_item, statusList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner.setAdapter(adapter)
        holder.spinner.setOnItemSelectedListener(this)
/*        holder.button_sign_up.setOnClickListener {
            Log.d(TAG, "onClick: clicked on button_sign_up")

            postData(examsList[position].id)
            notifyDataSetChanged()

        }*/
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

    class ExamsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val id:TextView = itemView.findViewById(R.id.id)!!
        val classroom_code:TextView = itemView.findViewById(R.id.classroom_code)!!
        val date:TextView = itemView.findViewById(R.id.date)!!
        val beginning:TextView = itemView.findViewById(R.id.beginning)!!
        val ending:TextView = itemView.findViewById(R.id.ending)!!
        val classroom_campus:TextView = itemView.findViewById(R.id.classroom_campus)!!
        val button_sign_up:Button = itemView.findViewById(R.id.button_sign_up)!!
        val parentLayout:ConstraintLayout = itemView.findViewById(R.id.exams_parent_layout)!!
        val spinner: Spinner = itemView.findViewById(R.id.spinner)
        val examiner: TextView = itemView.findViewById(R.id.examiner)
    }

    private fun postData(id: String){
        Log.d(TAG, "postData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val token = preferences.getString("token", "")
        val username = preferences.getString("username", "")
        val path = "api/finales"
        val params = JSONObject()
        params.put("student", username)
        params.put("id", id)
        params.put("status", status)
        Log.d(TAG, "Params: ${params}")

        apiController.post(path, token, params){ response ->
            Log.d(TAG, response.toString())
            val intent = Intent(context, SubjectsActivity::class.java)
            context.startActivity(intent)
        }
    }
}