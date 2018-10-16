package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.matacos.mataco.clases.Subject

class ExamSubjectsAdapter(val context: Context, val examSubjectsList: ArrayList<Subject>, val preferences: SharedPreferences) : RecyclerView.Adapter<ExamSubjectsAdapter.ExamSubjectsViewHolder>() {

    private val TAG: String = ExamSubjectsAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: ExamSubjectsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.name.text = examSubjectsList[position].name
        holder.department_code.text = examSubjectsList[position].subject()
        holder.parentLayout.setOnClickListener {
            Log.d(TAG, "onClick: clicked on: " + examSubjectsList[position].name)

            val editPreferences = preferences.edit()
            editPreferences.putString("subject_department", examSubjectsList[position].department)
            editPreferences.putString("subject_code", examSubjectsList[position].code)
            editPreferences.putString("subject_name", examSubjectsList[position].name)
            editPreferences.apply()
            Log.d(TAG, "startActivity: ExamsActivity")
            val intent = Intent(context, ExamsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamSubjectsViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.subject_item_layout, parent, false)
        return ExamSubjectsViewHolder(v)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return examSubjectsList.size
    }

    class ExamSubjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)!!
        val department_code: TextView = itemView.findViewById(R.id.department_code)!!
        val parentLayout: android.support.constraint.ConstraintLayout = itemView.findViewById(R.id.subject_parent_layout)!!
    }
}