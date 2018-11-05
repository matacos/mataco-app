package com.matacos.mataco

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.matacos.mataco.clases.StudentRecord

class StudentRecordAdapter(val context: Context, val studentRecord: ArrayList<StudentRecord>, val preferences: SharedPreferences) : androidx.recyclerview.widget.RecyclerView.Adapter<StudentRecordAdapter.StudentRecordViewHolder>() {

    private val TAG: String = StudentRecordAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: StudentRecordViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.code.text = studentRecord[position].subject()
        holder.name.text = studentRecord[position].name
        holder.date.text = studentRecord[position].date()
        holder.result.text = studentRecord[position].result()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentRecordViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.student_record_item_layout, parent, false)
        return StudentRecordViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return studentRecord.size
    }

    class StudentRecordViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val code: TextView = itemView.findViewById<TextView>(R.id.code)!!
        val name: TextView = itemView.findViewById<TextView>(R.id.name)!!
        val date: TextView = itemView.findViewById(R.id.date)!!
        val result: TextView = itemView.findViewById(R.id.result)!!
    }
}