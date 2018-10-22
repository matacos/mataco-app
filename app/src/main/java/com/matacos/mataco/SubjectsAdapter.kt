package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.matacos.mataco.clases.Subject

class SubjectsAdapter(val context: Context, val subjectsList: ArrayList<Subject>, val preferences: SharedPreferences) : RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder>() {

    private val TAG: String = SubjectsAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.name.text = subjectsList[position].name
        holder.department_code.text = subjectsList[position].subject()
        holder.parentLayout.setOnClickListener {
            Log.d(TAG, "onClick: clicked on: " + subjectsList[position].name)

            val editPreferences: SharedPreferences.Editor = preferences.edit()
            editPreferences.putString("subject_department", subjectsList[position].department)
            editPreferences.putString("subject_code", subjectsList[position].code)
            editPreferences.putString("subject_name", subjectsList[position].name)
            editPreferences.putBoolean("subject_enroled", subjectsList[position].enroled)
            editPreferences.apply()
            Log.d(TAG, "startActivity: CoursesActivity")
            val intent = Intent(context, CoursesActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.subject_item_layout, parent, false)
        return SubjectsViewHolder(v)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return subjectsList.size
    }

    class SubjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)!!
        val department_code: TextView = itemView.findViewById(R.id.department_code)!!
        val parentLayout: ConstraintLayout = itemView.findViewById(R.id.subject_parent_layout)!!
    }
}