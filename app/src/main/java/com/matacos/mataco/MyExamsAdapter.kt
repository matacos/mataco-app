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
import com.matacos.mataco.clases.ExamInscription

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyExamsAdapter(val context: Context, val examsList: ArrayList<ExamInscription>, val preferences: SharedPreferences) : androidx.recyclerview.widget.RecyclerView.Adapter<MyExamsAdapter.MyExamsViewHolder>() {

    private val TAG: String = MyExamsAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: MyExamsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.code.text = examsList[position].subject()
        holder.name.text = examsList[position].subject.name
        holder.classroomCode.text = examsList[position].classroom_code
        holder.date.text = examsList[position].date()
        holder.beginning.text = examsList[position].beginning()
        holder.ending.text = examsList[position].ending()
        holder.examiner.text = examsList[position].examiner.toString()
        holder.classroomCampus.text = examsList[position].classroom_campus
        holder.state.text = examsList[position].status()
        val dropOutExams: Boolean = preferences.getBoolean("drop_out_exams", false)

        holder.buttonDropOut.setOnClickListener {
            Log.d(TAG, "onClick: clicked on button_drop_out")

            deleteData(examsList[position].id.toString(), position)
        }

        if (!dropOutExams) {
            holder.buttonDropOut.isEnabled = false
        } else {
            holder.buttonDropOut.isEnabled = true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyExamsViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_exams_item_layout2, parent, false)
        return MyExamsViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return examsList.size
    }

    class MyExamsViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById<TextView>(R.id.name)!!
        val code: TextView = itemView.findViewById<TextView>(R.id.code)!!
        val classroomCode: TextView = itemView.findViewById(R.id.classroom_code)!!
        val date: TextView = itemView.findViewById(R.id.date)!!
        val beginning: TextView = itemView.findViewById(R.id.beginning)!!
        val ending: TextView = itemView.findViewById(R.id.ending)!!
        val classroomCampus: TextView = itemView.findViewById(R.id.classroom_campus)!!
        val buttonDropOut: Button = itemView.findViewById(R.id.button_drop_out)!!
        val state: TextView = itemView.findViewById(R.id.state)
        val examiner: TextView = itemView.findViewById(R.id.examiner)
    }


    private fun deleteData(id: String, position: Int) {
        Log.d(TAG, "deleteData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val token: String = preferences.getString("token", "")
        val username: String = preferences.getString("username", "")
        val path = "api/inscripciones_final/$id-$username"

        apiController.delete(path, token) { response ->
            Log.d(TAG, response.toString())

            examsList.removeAt(position)
            notifyDataSetChanged()
            if (examsList.isEmpty()) {
                val intent = Intent(context, MyExamsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}