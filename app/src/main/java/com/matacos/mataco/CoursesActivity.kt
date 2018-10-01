package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_courses.*
import kotlinx.android.synthetic.main.content_subjects.*
import java.sql.Time

class CoursesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = CoursesActivity::class.java.simpleName
    val courses = ArrayList<Course>()
    val displayedCourses = ArrayList<Course>()
    private var enrolled = false
    private var semester = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)
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

        courses_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        courses_recycler_view.adapter = CoursesAdapter(this, displayedCourses, getSharedPreferences("my_preferences", Context.MODE_PRIVATE), enrolled)

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
            val editext = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
            editext.hint = "Buscar curso..."

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
                            val professor = it.professors.toLowerCase()
                            if (professor.contains(search)) {
                                Log.d(TAG, "professor.contains(search)")
                                displayedCourses.add(it)
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
    }


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

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadData() {
        Log.d(TAG, "loadData")


        val service = ServiceVolley()
        val apiController = APIController(service)


        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token = preferences.getString("token", "")

            Log.d(TAG, "BB: Here2")
            val department = preferences.getString("subject_department", "")
            val code = preferences.getString("subject_code", "")
            //val path = "api/cursos?cod_departamento=$department&cod_materia=$code"
            val path = "api/cursos?cod_departamento=75&cod_materia=07"
            apiController.get(path, token) { response ->
                Log.d(TAG, response.toString())
                if (response != null) {
/*                    val editPreferences = preferences.edit()
                    editPreferences.putString("token", response.getString("token")).apply()*/

                    val jSONCourses= response.getJSONArray("courses")

//                    val semester = jSONCourses.getJSONObject(0).getString("semester")
                    val screenTitle = findViewById<TextView>(R.id.screen_title)
//                    screenTitle.text = "Oferta Académica Cuatrimestre ${semester.substring(0,1)} de ${semester.substring(2)}"
                    screenTitle.text = "Oferta Académica Cuatrimestre 2 de 2018"

                    val departmentCode = jSONCourses.getJSONObject(0).getString("department_code")
                    val subjectCode = jSONCourses.getJSONObject(0).getString("subject_code")
                    val editPreferences = preferences.edit()
                    editPreferences.putBoolean(departmentCode + subjectCode, false).apply()
                    for (j in 0 until jSONCourses.length()) {
                        Log.d(TAG, "parsing data 2")

                        val enrolledInSubject = jSONCourses.getJSONObject(j).getString("enroled").toBoolean()
                        Log.d(TAG, "enrolledInSubject: $enrolledInSubject en ${departmentCode + subjectCode}")
                        if(enrolledInSubject) {
                            editPreferences.putBoolean(departmentCode + subjectCode, enrolledInSubject).apply()
                            val alreadyIn = findViewById<TextView>(R.id.already_one_of_your_subjects)
                            alreadyIn.visibility = View.VISIBLE
                        }

                        var professors = ""
                        for (k in 0 until jSONCourses.getJSONObject(j).getJSONArray("professors").length()) {
                            val professor = jSONCourses.getJSONObject(j).getJSONArray("professors").getJSONObject(k)
                            professors += "${professor.getString("name")} ${professor.getString("surname")}, "
                        }
                        professors = professors.trim().trimEnd(',')
                        val timeSlots = ArrayList<TimeSlot>()
                        for (k in 0 until jSONCourses.getJSONObject(j).getJSONArray("time_slots").length()) {
                            val timeSlot = jSONCourses.getJSONObject(j).getJSONArray("time_slots").getJSONObject(k)
                            Log.d(TAG, "parsing data 3")
                            timeSlots.add(TimeSlot(  timeSlot.getString("classroom_code"),
                                                    timeSlot.getString("classroom_campus"),
                                    timeSlot.getString("beginning"),
                                    timeSlot.getString("ending"),
                                    timeSlot.getString("day_of_week"),
                                    timeSlot.getString("description")))
                        }

                        Log.d(TAG, "parsing data 4")
                        courses.add(Course(jSONCourses.getJSONObject(j).getString("department_code"),
                                jSONCourses.getJSONObject(j).getString("subject_code"),
                                jSONCourses.getJSONObject(j).getString("name"),
                                jSONCourses.getJSONObject(j).getString("course"),
                                jSONCourses.getJSONObject(j).getString("free_slots"),
                                professors,
                                timeSlots[0].classroomCampus,
                                jSONCourses.getJSONObject(j).getString("enroled").toBoolean(),
                                false,
                                timeSlots
                        ))
                        Log.d(TAG, "parsing data 5")
                    }
                    courses.sort()
                    Log.d(TAG, "parsing data 6")
                    displayedCourses.addAll(courses.distinct())
                    Log.d(TAG, "parsing data 7")
                    courses_recycler_view.adapter!!.notifyDataSetChanged()
                    Log.d(TAG, "parsing data 8")
                }


        }
    }

}
