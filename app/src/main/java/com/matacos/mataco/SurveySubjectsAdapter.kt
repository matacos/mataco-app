package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.matacos.mataco.clases.Subject
import com.matacos.mataco.clases.SurveySubject

class SurveySubjectsAdapter(val context: Context, val subjectsList: ArrayList<SurveySubject>, val preferences: SharedPreferences) : androidx.recyclerview.widget.RecyclerView.Adapter<SurveySubjectsAdapter.SurveySubjectsViewHolder>() {

    private val TAG: String = SurveySubjectsAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: SurveySubjectsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.semester.text = subjectsList[position].semester()
        holder.name.text = subjectsList[position].name
        holder.department_code.text = subjectsList[position].subject()
        holder.parentLayout.setOnClickListener {
            Log.d(TAG, "onClick: clicked on: " + subjectsList[position].name)

            val editPreferences: SharedPreferences.Editor = preferences.edit()
            editPreferences.putString("subject_department", subjectsList[position].department)
            editPreferences.putString("subject_code", subjectsList[position].code)
            editPreferences.putString("subject_name", subjectsList[position].name)
            editPreferences.putInt("subject_course", subjectsList[position].course)
            editPreferences.apply()
            Log.d(TAG, "startActivity: SurveysActivity")
            val intent = Intent(context, SurveyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveySubjectsViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.survey_subject_item_layout, parent, false)
        return SurveySubjectsViewHolder(v)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return subjectsList.size
    }

    class SurveySubjectsViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val semester: TextView = itemView.findViewById(R.id.semester)!!
        val name: TextView = itemView.findViewById(R.id.name)!!
        val department_code: TextView = itemView.findViewById(R.id.department_code)!!
        val parentLayout: ConstraintLayout = itemView.findViewById(R.id.subject_parent_layout)!!
    }
}