package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.*
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_my_exams.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyExamsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = MyExamsActivity::class.java.simpleName
    val exams = ArrayList<ExamInscription>()
    val displayedExams = ArrayList<ExamInscription>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Entro a activity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_exams)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        my_exams_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        my_exams_recycler_view.adapter = MyExamsAdapter(this, displayedExams, getSharedPreferences("my_preferences", Context.MODE_PRIVATE))

        swipe_refresh_content_my_exams.setOnRefreshListener {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.search)
        if (searchItem != null) {
            Log.d(TAG, "searchItem != null")
            val searchView = searchItem.actionView as SearchView
            val editext = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editext.hint = "Buscar examen..."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "onQueryTextSubmit")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "onQueryTextChange")
                    displayedExams.clear()
                    if (newText!!.isNotEmpty()) {
                        Log.d(TAG, "newText!!.isNotEmpty()")
                        val search = newText.toLowerCase()
                        exams.forEach {
                            Log.d(TAG, "exams.forEach")
                            val subject = it.department_code.toLowerCase() + "." + it.subject_code
                            if (subject.contains(search)) {
                                Log.d(TAG, "exam.contains(search)")
                                displayedExams.add(it)
                            }
                        }
                    } else {
                        displayedExams.addAll(exams.distinct())
                    }
                    my_exams_recycler_view.adapter!!.notifyDataSetChanged()
                    return true
                }

            })
        }
        return super.onCreateOptionsMenu(menu)
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

    private fun addEmptyListText(exams: ArrayList<ExamInscription>) {
        Log.d(TAG, "addEmptyListText")
        if (exams.isEmpty()) {
            no_available_exams.visibility = View.VISIBLE
        } else {
            no_available_exams.visibility = View.GONE
        }
    }

    private fun addCantDropOutText(canDropOutExams: Boolean) {
        Log.d(TAG, "addCantSignUpText")
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editPreferences = preferences.edit()
        editPreferences.putBoolean("drop_out_exams", canDropOutExams).apply()
        if (!canDropOutExams && !exams.isEmpty()) {
            cant_drop_out_exams.visibility = View.VISIBLE
        } else {
            cant_drop_out_exams.visibility = View.GONE
        }
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

        exams.clear()
        displayedExams.clear()

        my_exams_recycler_view.adapter!!.notifyDataSetChanged()

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

                    val username: String = preferences.getString("username", "")

                    path = "api/inscripciones_final?estudiante=$username"

                    apiController.get(path, token) { response ->
                        Log.d(TAG, response.toString())
                        if (response != null) {

                            val examInscriptions = gson.fromJson(response.toString(), ExamInscriptions::class.java)
                            for (exam in examInscriptions.examInscriptions) {
                                exam.exam.status = exam.state
                                exams.add(exam.exam)
                            }
                            exams.sort()
                            displayedExams.addAll(exams.distinct())

                            addEmptyListText(exams)
                            addCantDropOutText(semester.canViewExams)

                            my_exams_recycler_view.adapter!!.notifyDataSetChanged()

                        } else {
                            Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    addEmptyListText(exams)
                }
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
        swipe_refresh_content_my_exams.isRefreshing = false
    }

}
