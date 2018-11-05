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
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.Subject
import com.matacos.mataco.clases.Subjects
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_subjects.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SubjectsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = SubjectsActivity::class.java.simpleName
    val subjects = ArrayList<Subject>()
    val displayedSubjects = ArrayList<Subject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects)
        setSupportActionBar(toolbar)

//        FirebaseMessaging.getInstance().subscribeToTopic("exams")

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        subjects_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        subjects_recycler_view.adapter = SubjectsAdapter(this, displayedSubjects, getSharedPreferences("my_preferences", Context.MODE_PRIVATE))

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
            editext.hint = "Buscar materia..."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "onQueryTextSubmit")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "onQueryTextChange")
                    displayedSubjects.clear()
                    if (newText!!.isNotEmpty()) {
                        Log.d(TAG, "newText!!.isNotEmpty()")
                        val search = newText.toLowerCase()
                        subjects.forEach {
                            Log.d(TAG, "subjects.forEach")
                            val subject = it.department.toLowerCase() + "." + it.code
                            if (subject.contains(search) || it.name.toLowerCase().contains(search)) {
                                Log.d(TAG, "subject.contains(search)")
                                displayedSubjects.add(it)
                            }
                        }
                    } else {
                        displayedSubjects.addAll(subjects.distinct())
                    }
                    subjects_recycler_view.adapter!!.notifyDataSetChanged()
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

    private fun filterSubjects(examSubjects: List<Subject>): ArrayList<Subject> {
        val filteredExamSubjects = ArrayList<Subject>()
        for (examSubject: Subject in examSubjects) {
            if (!examSubject.approved) {
                filteredExamSubjects.add(examSubject)
            }
        }
        return filteredExamSubjects
    }

    private fun loadData() {
        Log.d(TAG, "loadData")


        val service = ServiceVolley()
        val apiController = APIController(service)


        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")
        val careerId: String = preferences.getString("career", null)

        val path = "api/materias?carrera=" + careerId

        apiController.get(path, token) { response ->
            Log.d(TAG, response.toString())
            if (response != null) {

                val gson = Gson()
                val gsonSubjects = gson.fromJson(response.toString(), Subjects::class.java)

                subjects.addAll(filterSubjects(gsonSubjects.subjects))
                subjects.sort()
                displayedSubjects.addAll(subjects)

                subjects_recycler_view.adapter!!.notifyDataSetChanged()
            }
        }
    }
}