package com.matacos.mataco

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.NavigationItemManager
import com.matacos.mataco.clases.SurveySubject
import com.matacos.mataco.clases.SurveySubjects

import kotlinx.android.synthetic.main.activity_survey_subjects.*
import kotlinx.android.synthetic.main.app_bar_survey_subjects.*
import kotlinx.android.synthetic.main.content_survey_subjects.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SurveySubjectsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = SurveySubjectsActivity::class.java.simpleName
    val subjects = ArrayList<SurveySubject>()
    val displayedSubjects = ArrayList<SurveySubject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_subjects)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        survey_subjects_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        survey_subjects_recycler_view.adapter = SurveySubjectsAdapter(this, displayedSubjects, getSharedPreferences("my_preferences", Context.MODE_PRIVATE))

        swipe_refresh_content_subjects.setOnRefreshListener {
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
                            if (it.subject().contains(search) || it.name.toLowerCase().contains(search)) {
                                Log.d(TAG, "subject.contains(search)")
                                displayedSubjects.add(it)
                            }
                        }
                    } else {
                        displayedSubjects.addAll(subjects.distinct())
                    }
                    survey_subjects_recycler_view.adapter!!.notifyDataSetChanged()
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

    private fun addEmptyListText(subjects:ArrayList<SurveySubject>) {
        Log.d(TAG, "addEmptyListText")
        if (subjects.isEmpty()) {
            no_available_subjects.visibility = View.VISIBLE
        }
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

        subjects.clear()
        displayedSubjects.clear()

        survey_subjects_recycler_view.adapter!!.notifyDataSetChanged()

        val service = ServiceVolley()
        val apiController = APIController(service)


        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")

        val username = preferences.getString("username", "")

        val path = "api/pending_polls?estudiante=$username"

        apiController.get(path, token) { response ->
            Log.d(TAG, response.toString())

            if (response != null) {

                val gson = Gson()
                val gsonSubjects = gson.fromJson(response.toString(), SurveySubjects::class.java)

                subjects.addAll(gsonSubjects.subjects)
                subjects.sort()
                displayedSubjects.addAll(subjects)

                addEmptyListText(subjects)

                survey_subjects_recycler_view.adapter!!.notifyDataSetChanged()

            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
            swipe_refresh_content_subjects.isRefreshing = false
        }
    }
}