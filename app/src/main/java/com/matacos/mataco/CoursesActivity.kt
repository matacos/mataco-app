package com.matacos.mataco

import android.content.Context
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
import kotlinx.android.synthetic.main.content_courses.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CoursesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = CoursesActivity::class.java.simpleName
    val courses = ArrayList<Course>()
    val displayedCourses = ArrayList<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)
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

        courses_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        courses_recycler_view.adapter = CoursesAdapter(this, displayedCourses, getSharedPreferences("my_preferences", Context.MODE_PRIVATE))

        swipe_refresh_content_courses.setOnRefreshListener {
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
                            val professor = it.professors().toLowerCase()
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
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val navigationItemManager = NavigationItemManager()
        return navigationItemManager.navigate(this, item, preferences, drawer_layout)
    }

    private fun verifyEnrollment() {
        Log.d(TAG, "verifyEnrollment")
        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val enroled: Boolean = preferences.getBoolean("subject_enroled", false)
        if (enroled) {
            already_one_of_your_subjects.visibility = View.VISIBLE
        } else {
            already_one_of_your_subjects.visibility = View.GONE
        }
    }

    private fun addEmptyListText() {
        Log.d(TAG, "verifyEnrollment")
        if (courses.isEmpty()) {
            no_available_courses.visibility = View.VISIBLE
        } else {
            no_available_courses.visibility = View.GONE
        }
    }

    private fun addClassroomCampus() {
        Log.d(TAG, "addClassroomCampus")
        for (course: Course in courses) {
            if (!course.timeSlots.isEmpty()) {
                course.classroomCampus = course.timeSlots[0].classroomCampus
            }
        }
    }

    private fun addCantSignUpText(canSignUpCourses: Boolean) {
        Log.d(TAG, "addCantSignUpText")
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editPreferences = preferences.edit()
        editPreferences.putBoolean("sign_up_courses", canSignUpCourses).apply()
        if (!canSignUpCourses && !courses.isEmpty()) {
            cant_sing_up_courses.visibility = View.VISIBLE
        } else {
            cant_sing_up_courses.visibility = View.GONE
        }
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

        courses.clear()
        displayedCourses.clear()

        courses_recycler_view.adapter!!.notifyDataSetChanged()

        val service = ServiceVolley()
        val apiController = APIController(service)

        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")

        var path = "api/ciclo_lectivo_actual"

        apiController.get("api/ciclo_lectivo_actual", token) { periodRsponse ->
            Log.d(TAG, "periodRsponse: " + periodRsponse.toString())

            if (periodRsponse != null) {
                val gson = Gson()
                val semesters: Semesters = gson.fromJson(periodRsponse.toString(), Semesters::class.java)

                if (!semesters.semesters.isEmpty()) {
                    val semester: Semester = semesters.semesters.get(0)
                    screen_title.text = semester.courses()

                    if (semester.canCheckAvailableCourses) {
                        val department: String = preferences.getString("subject_department", "")
                        val code: String = preferences.getString("subject_code", "")

                        path = "api/cursos?cod_departamento=$department&cod_materia=$code"

                        apiController.get(path, token) { response ->
                            Log.d(TAG, response.toString())

                            if (response != null) {
                                val coursesSubjects: Courses = gson.fromJson(response.toString(), Courses::class.java)
                                courses.addAll(coursesSubjects.courses)
                                courses.sort()
                                displayedCourses.addAll(courses)

                                addClassroomCampus()
                                verifyEnrollment()
                                addEmptyListText()
                                addCantSignUpText(semester.canSignUpCourses)

                                courses_recycler_view.adapter!!.notifyDataSetChanged()

                            } else {
                                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        addEmptyListText()
                    }
                } else {
                    screen_title.text = ""
                    addEmptyListText()
                }
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
        swipe_refresh_content_courses.isRefreshing = false
    }
}
