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
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.Course
import com.matacos.mataco.clases.CourseInscriptions
import com.matacos.mataco.clases.Courses
import com.matacos.mataco.clases.TimeSlot
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_my_courses.*

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

        my_courses_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
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

        //TODO: Add request for the semester
        //val semester = jSONCourses.getJSONObject(0).getString("semester")
        val screenTitle = findViewById<TextView>(R.id.screen_title)
        //screenTitle.text = "Oferta Académica Cuatrimestre ${semester.substring(0,1)} de ${semester.substring(2)}"
        screenTitle.text = "Cuatrimestre 2 de 2018"

        val service = ServiceVolley()
        val apiController = APIController(service)
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token = preferences.getString("token", "")
        val username = preferences.getString("username", "")
        val path = "api/cursadas?estudiante=$username"

        apiController.get(path, token) { response ->
            Log.d(TAG, response.toString())
            if (response != null) {
/*                    val editPreferences = preferences.edit()
                    editPreferences.putString("token", response.getString("token")).apply()*/
                Log.d(TAG, "Parsing 1")
                val gson = Gson()
                Log.d(TAG, "Parsing 2")
                val coursesSubjects = gson.fromJson(response.toString(), CourseInscriptions::class.java)
                Log.d(TAG, "Parsing 3")
                for (course in coursesSubjects.courses) {
                    Log.d(TAG, "Parsing 4")
                    courses.add(course.course)
                    Log.d(TAG, "Parsing 5")
                }
                Log.d(TAG, "Parsing 6")
                courses.sort()
                Log.d(TAG, "Parsing 7")
                Log.d(TAG, courses.toString())
                Log.d(TAG, "Parsing 8")
                displayedCourses.addAll(courses.distinct())
                Log.d(TAG, "Parsing 9")
                my_courses_recycler_view.adapter!!.notifyDataSetChanged()
            }


        }
    }

}
