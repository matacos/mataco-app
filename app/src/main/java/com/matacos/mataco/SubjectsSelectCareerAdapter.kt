package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.matacos.mataco.clases.Career

class SubjectsSelectCareerAdapter(val context: Context, val careers: ArrayList<Career>, val preferences: SharedPreferences) : androidx.recyclerview.widget.RecyclerView.Adapter<SubjectsSelectCareerAdapter.SubjectsSelectCareerViewHolder>() {

    private val TAG: String = SubjectsSelectCareerAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: SubjectsSelectCareerViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        holder.career.text = careers[position].career()

        Glide.with(context)
                .load(Uri.parse(careers[position].icon()))
                .into(holder.icon)

        holder.parentLayout.setOnClickListener {
            Log.d(TAG, "onClick: clicked on: " + careers[position].career())

            val editPreferences: SharedPreferences.Editor = preferences.edit()
            editPreferences.putString("career", careers[position].code).apply()
            Log.d(TAG, "startActivity: SubjectsActivity")
            val intent = Intent(context, SubjectsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsSelectCareerViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.subject_select_career_item_layout, parent, false)
        return SubjectsSelectCareerViewHolder(v)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount")
        return careers.size
    }

    class SubjectsSelectCareerViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val career: TextView = itemView.findViewById(R.id.career)!!
        val icon: ImageView = itemView.findViewById(R.id.icon)!!
        val parentLayout: ConstraintLayout = itemView.findViewById(R.id.subject_select_career_parent_layout)!!
    }
}