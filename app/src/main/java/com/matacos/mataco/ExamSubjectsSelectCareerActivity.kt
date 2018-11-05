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
import com.google.gson.Gson
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.Career
import com.matacos.mataco.clases.Subject
import com.matacos.mataco.clases.Subjects
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_subjects.*
import kotlinx.android.synthetic.main.content_subjects_select_career.*

class ExamSubjectsSelectCareerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = ExamSubjectsSelectCareerActivity::class.java.simpleName
    val careers = ArrayList<Career>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_subjects_select_career)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        subjects_select_career_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        subjects_select_career_recycler_view.adapter = ExamSubjectsSelectCareerAdapter(this, careers, getSharedPreferences("my_preferences", Context.MODE_PRIVATE))

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
                applicationContext.startActivity(intent)
            }
            R.id.nav_courses -> {
                val intent = Intent(applicationContext, MyCoursesActivity::class.java)
                applicationContext.startActivity(intent)
            }
            R.id.nav_exam_subjects -> {
                val intent = Intent(applicationContext, ExamSubjectsSelectCareerActivity::class.java)
                applicationContext.startActivity(intent)
            }
            R.id.nav_my_exams -> {
                val intent = Intent(applicationContext, MyExamsActivity::class.java)
                applicationContext.startActivity(intent)
            }
            R.id.nav_student_record -> {
                val intent = Intent(applicationContext, StudentRecordActivity::class.java)
                applicationContext.startActivity(intent)
            }
            R.id.nav_log_out -> {
                val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                val editPreferences = preferences.edit()
                editPreferences.clear().apply()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                applicationContext.startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val careerIds = preferences.getStringSet("career_ids", null)

        for (careerId in careerIds) {
            Log.d(TAG, "CareerId: " + careerId)
            val career = Career(careerId)
            careers.add(career)
        }
        careers.sort()
        Log.d(TAG, "Career 0: " + careers[0].code + " "+ careers[0].name())
        subjects_select_career_recycler_view.adapter!!.notifyDataSetChanged()
    }


}

