package com.matacos.mataco

import android.content.Context
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
import com.matacos.mataco.clases.ExamInscription

class MyExamsAdapter(val context: Context, val examsList: ArrayList<ExamInscription>, val preferences: SharedPreferences): RecyclerView.Adapter<MyExamsAdapter.MyExamsViewHolder>() {

    private val TAG: String = MyExamsAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: MyExamsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.code.text = examsList[position].subject.subject()
        holder.name.text = examsList[position].subject_name()
        holder.classroomCode.text = examsList[position].classroom_code
        holder.date.text = examsList[position].date()
        holder.beginning.text = examsList[position].beginning()
        holder.ending.text = examsList[position].ending()
        holder.examiner.text = examsList[position].examiner.toString()
        holder.classroomCampus.text = examsList[position].classroom_campus
        holder.state.text = examsList[position].status
        holder.buttonDropOut.setOnClickListener {
            Log.d(TAG, "onClick: clicked on button_drop_out")

//            deleteData(examsList[position].number, position)
            /*val intent = Intent(context, MyCoursesActivity::class.java)
            context.startActivity(intent)*/
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

    class MyExamsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)!!
        val code = itemView.findViewById<TextView>(R.id.code)!!
        val classroomCode:TextView = itemView.findViewById(R.id.classroom_code)!!
        val date:TextView = itemView.findViewById(R.id.date)!!
        val beginning:TextView = itemView.findViewById(R.id.beginning)!!
        val ending:TextView = itemView.findViewById(R.id.ending)!!
        val classroomCampus:TextView = itemView.findViewById(R.id.classroom_campus)!!
        val buttonDropOut:Button = itemView.findViewById(R.id.button_drop_out)!!
        val parentLayout: ConstraintLayout = itemView.findViewById(R.id.exams_parent_layout)!!
        val state: TextView = itemView.findViewById(R.id.state)
        val examiner: TextView = itemView.findViewById(R.id.examiner)
    }

    private fun deleteData(course: String, position: Int){
        Log.d(TAG, "deleteData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val token = preferences.getString("token", "")
        val username = preferences.getString("username", "")
        val path = "api/cursadas/$course-$username"

        apiController.delete(path, token){ response ->
            Log.d(TAG, response.toString())

            examsList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}