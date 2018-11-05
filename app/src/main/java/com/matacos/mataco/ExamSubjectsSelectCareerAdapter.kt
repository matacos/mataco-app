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
import com.matacos.mataco.clases.Career

class ExamSubjectsSelectCareerAdapter(val context: Context, val careers: ArrayList<Career>, val preferences: SharedPreferences) : androidx.recyclerview.widget.RecyclerView.Adapter<ExamSubjectsSelectCareerAdapter.ExamSubjectsSelectCareerViewHolder>() {

    private val TAG: String = ExamSubjectsSelectCareerAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: ExamSubjectsSelectCareerViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.code.text = careers[position].code
        holder.name.text = careers[position].name()

        holder.parentLayout.setOnClickListener {
            Log.d(TAG, "onClick: clicked on: " + careers[position].name())

            val editPreferences: SharedPreferences.Editor = preferences.edit()
            editPreferences.putString("career", careers[position].code).apply()
            Log.d(TAG, "startActivity: ExamSubjectsActivity")
            val intent = Intent(context, ExamSubjectsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamSubjectsSelectCareerViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.subject_select_career_item_layout, parent, false)
        return ExamSubjectsSelectCareerViewHolder(v)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return careers.size
    }

    class ExamSubjectsSelectCareerViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val code: TextView = itemView.findViewById(R.id.code)!!
        val name: TextView = itemView.findViewById(R.id.name)!!
        val parentLayout: ConstraintLayout = itemView.findViewById(R.id.subject_select_career_parent_layout)!!
    }
}