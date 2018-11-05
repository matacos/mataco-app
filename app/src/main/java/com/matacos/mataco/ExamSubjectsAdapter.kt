package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.matacos.mataco.clases.Subject

class ExamSubjectsAdapter(val context: Context, val examSubjectsList: ArrayList<Subject>, val preferences: SharedPreferences) : androidx.recyclerview.widget.RecyclerView.Adapter<ExamSubjectsAdapter.ExamSubjectsViewHolder>() {

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
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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

    class ExamSubjectsViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)!!
        val department_code: TextView = itemView.findViewById(R.id.department_code)!!
        val parentLayout: ConstraintLayout = itemView.findViewById(R.id.subject_parent_layout)!!
    }
}