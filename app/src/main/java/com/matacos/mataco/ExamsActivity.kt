package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.Exam
import com.matacos.mataco.clases.Exams
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_exams.*
import androidx.recyclerview.widget.RecyclerView


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ExamsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = ExamsActivity::class.java.simpleName
    private var exams = ArrayList<Exam>()
    private var displayedExams = ArrayList<Exam>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exams)
        setSupportActionBar(toolbar)

        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        val department: String = preferences.getString("subject_department", "")
        val code: String = preferences.getString("subject_code", "")
        val name: String = preferences.getString("subject_name", "")

        supportActionBar?.title = "$department.$code $name"

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        exams_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        exams_recycler_view.adapter = ExamsAdapter(this, displayedExams, getSharedPreferences("my_preferences", Context.MODE_PRIVATE))

        loadData()

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_subjects -> {
                val intent = Intent(applicationContext, SubjectsSelectCareerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)
            }
            R.id.nav_courses -> {
                val intent = Intent(applicationContext, MyCoursesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)
            }
            R.id.nav_exam_subjects -> {
                val intent = Intent(applicationContext, ExamSubjectsSelectCareerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)
            }
            R.id.nav_my_exams -> {
                val intent = Intent(applicationContext, MyExamsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)
            }
            R.id.nav_student_record -> {
                val intent = Intent(applicationContext, StudentRecordActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)
            }
            R.id.nav_log_out -> {
                val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                val editPreferences = preferences.edit()
                editPreferences.clear().apply()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun filterExams(exams: ArrayList<Exam>): ArrayList<Exam> {
        val filteredExams = ArrayList<Exam>()
        for (exam: Exam in exams) {
            if (!exam.enroled) {
                filteredExams.add(exam)
            }
        }
        return filteredExams
    }

    private fun addEmptyListText() {
        Log.d(TAG, "verifyEnrollment")
        if (exams.isEmpty()) {
            no_available_exams.visibility = View.VISIBLE
        }
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

        val service = ServiceVolley()
        val apiController = APIController(service)
        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")
        val department: String = preferences.getString("subject_department", "")
        val code: String = preferences.getString("subject_code", "")
        val path = "api/finales?cod_departamento=$department&cod_materia=$code"

        apiController.get(path, token) { response ->
            Log.d(TAG, response.toString())
            if (response != null) {

                val gson = Gson()
                val examsGson = gson.fromJson(response.toString(), Exams::class.java)
                for (exam in examsGson.exams) {
                    exams.add(exam)
                }
                exams.sort()
                addEmptyListText()
                displayedExams.addAll(filterExams(exams))
                exams_recycler_view.adapter!!.notifyDataSetChanged()
            }


        }
    }

}