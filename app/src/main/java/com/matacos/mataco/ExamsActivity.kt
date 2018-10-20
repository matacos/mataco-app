package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.Course
import com.matacos.mataco.clases.Courses
import com.matacos.mataco.clases.Exam
import com.matacos.mataco.clases.Exams
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_courses.*
import kotlinx.android.synthetic.main.content_exams.*

class ExamsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = ExamsActivity::class.java.simpleName
    var exams = ArrayList<Exam>()
    var displayedExams = ArrayList<Exam>()
    private var enrolled = false
    private var semester = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exams)
        setSupportActionBar(toolbar)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        val department = preferences.getString("subject_department", "")
        val code = preferences.getString("subject_code", "")
        val name = preferences.getString("subject_name", "")

        supportActionBar?.title = "$department.$code $name"

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        exams_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
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

/*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.search)
        if (searchItem != null) {
            Log.d(TAG, "searchItem != null")
            val searchView = searchItem.actionView as SearchView
            val editext = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
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
                            val professor = it.professors().toLowerCase()
                            if (professor.contains(search)) {
                                Log.d(TAG, "professor.contains(search)")
                                displayedExams.add(it)
                            }
                        }
                    } else {
                        displayedCourses.addAll(courses.distinct())
                    }
                    courses_recycler_view.adapter!!.notifyDataSetChanged()
                    return true
                }

            })
        }
        return super.onCreateOptionsMenu(menu)
    }*/


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_subjects -> {
                val intent = Intent(applicationContext, SubjectsActivity::class.java)
                applicationContext.startActivity(intent)
            }
            R.id.nav_courses -> {
                val intent = Intent(applicationContext, MyCoursesActivity::class.java)
                applicationContext.startActivity(intent)
            }
            R.id.nav_exam_subjects -> {
                val intent = Intent(applicationContext, ExamSubjectsActivity::class.java)
                applicationContext.startActivity(intent)
            }
            R.id.nav_my_exams -> {
                val intent = Intent(applicationContext, MyExamsActivity::class.java)
                applicationContext.startActivity(intent)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

/*    private fun verifyEnrollment(exams: ArrayList<Exam>) {
        Log.d(TAG, "verifyEnrollment")
        val departmentCode = exams.get(0).department_code
        val subjectCode = exams.get(0).subject_code
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editPreferences = preferences.edit()
        editPreferences.putBoolean(departmentCode + subjectCode, false).apply()
        for(course in courses) {
            val enrolledInSubject = course.enrolled
            Log.d(TAG, "enrolledInSubject: $enrolledInSubject en ${departmentCode + subjectCode}")
            if (enrolledInSubject) {
                editPreferences.putBoolean(departmentCode + subjectCode, enrolledInSubject).apply()
                val alreadyIn = findViewById<TextView>(R.id.already_one_of_your_subjects)
                alreadyIn.visibility = View.VISIBLE
            }
        }
    }*/

    private fun filterExams(exams:ArrayList<Exam>):ArrayList<Exam>{
        val filteredExams = ArrayList<Exam>()
        for (exam in exams){
            if (!exam.enroled){
                filteredExams.add(exam)
            }
        }
        return filteredExams
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

        //TODO: Add request for the semester
        //val semester = jSONCourses.getJSONObject(0).getString("semester")
        //val screenTitle = findViewById<TextView>(R.id.screen_title)
        //screenTitle.text = "Oferta Académica Cuatrimestre ${semester.substring(0,1)} de ${semester.substring(2)}"
        //screenTitle.text = "Oferta de Exámenes"

        val service = ServiceVolley()
        val apiController = APIController(service)
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token = preferences.getString("token", "")
        val department = preferences.getString("subject_department", "")
        val code = preferences.getString("subject_code", "")
        val path = "api/finales?cod_departamento=$department&cod_materia=$code"

        apiController.get(path, token) { response ->
            Log.d(TAG, response.toString())
            if (response != null) {
/*                    val editPreferences = preferences.edit()
                    editPreferences.putString("token", response.getString("token")).apply()*/
                val gson = Gson()
                val examsGson = gson.fromJson(response.toString(), Exams::class.java)
                for (exam in examsGson.exams) {
                    exams.add(exam)
                }
                exams.sort()
                //verifyEnrollment(courses)
                displayedExams.addAll(filterExams(exams))
                exams_recycler_view.adapter!!.notifyDataSetChanged()
            }


        }
    }

}