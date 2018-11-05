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
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.*
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_my_courses.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyCoursesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = MyCoursesActivity::class.java.simpleName
    val courses = ArrayList<Course>()
    val displayedCourses = ArrayList<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_courses)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        my_courses_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        my_courses_recycler_view.adapter = MyCoursesAdapter(this, displayedCourses, getSharedPreferences("my_preferences", Context.MODE_PRIVATE))

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
        val searchItem: MenuItem = menu.findItem(R.id.search)
        Log.d(TAG, "searchItem != null")
        val searchView: SearchView = searchItem.actionView as SearchView
        val editText: EditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        editText.hint = "Buscar curso..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange")
                displayedCourses.clear()
                if (newText!!.isNotEmpty()) {
                    Log.d(TAG, "newText!!.isNotEmpty()")
                    val search = newText.toLowerCase()
                    courses.forEach {
                        Log.d(TAG, "courses.forEach")
                        val professor = it.professors().toLowerCase()
                        if (professor.contains(search)) {
                            Log.d(TAG, "professor.contains(search)")
                            displayedCourses.add(it)
                        }
                    }
                } else {
                    displayedCourses.addAll(courses.distinct())
                }
                my_courses_recycler_view.adapter!!.notifyDataSetChanged()
                return true
            }

        })
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


    private fun addClassroomCampus() {
        Log.d(TAG, "addClassroomCampus")
        for (course: Course in courses) {
            course.classroomCampus = course.timeSlots[0].classroomCampus
        }
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

        //TODO: Add request for the semester
        //val semester = jSONCourses.getJSONObject(0).getString("semester")
        //screenTitle.text = "Oferta Académica Cuatrimestre ${semester.substring(0,1)} de ${semester.substring(2)}"
        screen_title.text = "Cuatrimestre 2 de 2018"

        val service = ServiceVolley()
        val apiController = APIController(service)
        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")
        val username: String = preferences.getString("username", "")
        val path = "api/cursadas?estudiante=$username"

        apiController.get(path, token) { response ->
            Log.d(TAG, response.toString())
            if (response != null) {
                val gson = Gson()
                val coursesSubjects: CourseInscriptions = gson.fromJson(response.toString(), CourseInscriptions::class.java)
                for (course: CoursesInscription in coursesSubjects.courses) {
                    courses.add(course.course)
                }
                courses.sort()
                addClassroomCampus()
                displayedCourses.addAll(courses.distinct())
                my_courses_recycler_view.adapter!!.notifyDataSetChanged()
            }


        }
    }

}
