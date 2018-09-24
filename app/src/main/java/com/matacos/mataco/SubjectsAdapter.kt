package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley

class SubjectsAdapter(val context: Context, val subjectsList: ArrayList<Subject>, val preferences: SharedPreferences): RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder>() {

    private val TAG: String = SubjectsAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.name.text = subjectsList[position].name
        holder.department_code.text = subjectsList[position].department + "." + subjectsList[position].code
       holder.parentLayout.setOnClickListener {
            Log.d(TAG, "onClick: clicked on: " + subjectsList[position].name)

           val editPreferences = preferences.edit()
           editPreferences.putString("subject", subjectsList[position].department + "." + subjectsList[position].code)
           editPreferences.apply()
           Log.d(TAG, "startActivity: CoursesActivity")
           val intent = Intent(context, CoursesActivity::class.java)
           context.startActivity(intent)
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.subject_item_layout, parent, false)
        return SubjectsViewHolder(v)
    }



    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return subjectsList.size
    }

    class SubjectsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)!!
        val department_code = itemView.findViewById<TextView>(R.id.department_code)!!
        val parentLayout = itemView.findViewById<android.support.constraint.ConstraintLayout>(R.id.subject_parent_layout)!!
    }
}