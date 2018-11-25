package com.matacos.mataco

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_exams.*
import androidx.recyclerview.widget.RecyclerView
import com.matacos.mataco.clases.*


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

        swipe_refresh_content_exams.setOnRefreshListener {
            loadData()
        }
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
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val navigationItemManager = NavigationItemManager()
        return navigationItemManager.navigate(this, item, preferences, drawer_layout)
    }

    private fun filterExams(exams: ArrayList<Exam>): ArrayList<Exam> {
        Log.d(TAG, "filterExams")
        val filteredExams = ArrayList<Exam>()
        for (exam: Exam in exams) {
            if (!exam.enroled) {
                filteredExams.add(exam)
            }
        }
        addEmptyListText(filteredExams)
        return filteredExams
    }

    private fun addEmptyListText(exams: ArrayList<Exam>) {
        Log.d(TAG, "addEmptyListText")
        if (exams.isEmpty()) {
            no_available_exams.visibility = View.VISIBLE
        } else {
            no_available_exams.visibility = View.GONE
        }
    }

    private fun addCantSignUpText(canSignUpExams: Boolean) {
        Log.d(TAG, "addCantSignUpText")
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editPreferences = preferences.edit()
        editPreferences.putBoolean("sign_up_exams", canSignUpExams).apply()
        if (!canSignUpExams && !displayedExams.isEmpty()) {
            cant_sing_up_exams.visibility = View.VISIBLE
        } else {
            cant_sing_up_exams.visibility = View.GONE
        }
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

        exams.clear()
        displayedExams.clear()

        exams_recycler_view.adapter!!.notifyDataSetChanged()

        val service = ServiceVolley()
        val apiController = APIController(service)

        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")

        var path = "api/ciclo_lectivo_actual"

        apiController.get(path, token) { periodRsponse ->
            Log.d(TAG, "periodRsponse: " + periodRsponse.toString())

            if (periodRsponse != null) {
                val gson = Gson()
                val semesters: Semesters = gson.fromJson(periodRsponse.toString(), Semesters::class.java)

                if (!semesters.semesters.isEmpty()) {
                    val semester: Semester = semesters.semesters.get(0)
                    val department: String = preferences.getString("subject_department", "")
                    val code: String = preferences.getString("subject_code", "")

                    path = "api/finales?cod_departamento=$department&cod_materia=$code"

                    apiController.get(path, token) { response ->
                        Log.d(TAG, response.toString())

                        if (response != null) {
                            val examsGson = gson.fromJson(response.toString(), Exams::class.java)
                            exams.addAll(examsGson.exams)
                            exams.sort()
                            displayedExams.addAll(filterExams(exams))

                            addCantSignUpText(semester.canViewExams)

                            exams_recycler_view.adapter!!.notifyDataSetChanged()

                        } else {
                            Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    addEmptyListText(displayedExams)
                }
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
        swipe_refresh_content_exams.isRefreshing = false
    }

}