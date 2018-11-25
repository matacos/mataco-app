package com.matacos.mataco

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.matacos.mataco.clases.Career
import com.matacos.mataco.clases.NavigationItemManager
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_subjects_select_career.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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

        subjects_select_career_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
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
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val navigationItemManager = NavigationItemManager()
        return navigationItemManager.navigate(this, item, preferences, drawer_layout)
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val careerIds: MutableSet<String> = preferences.getStringSet("career_ids", null)

        for (careerId in careerIds) {
            val career = Career(careerId)
            careers.add(career)
        }
        careers.sort()
        subjects_select_career_recycler_view.adapter!!.notifyDataSetChanged()
    }


}

