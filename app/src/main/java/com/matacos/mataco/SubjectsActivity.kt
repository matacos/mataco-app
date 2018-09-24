package com.matacos.mataco

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.support.v7.widget.SearchView
import android.widget.EditText
import android.widget.LinearLayout
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import kotlinx.android.synthetic.main.activity_subjects.*
import kotlinx.android.synthetic.main.app_bar_subjects.*
import kotlinx.android.synthetic.main.content_subjects.*

class SubjectsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = SubjectsActivity::class.java.simpleName
    val subjects = ArrayList<Subject>()
    val displayedSubjects = ArrayList<Subject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        subjects_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
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
        menuInflater.inflate(R.menu.menu_search_subjects, menu)
        val searchItem = menu.findItem(R.id.search_subjects)
        if (searchItem != null) {
            Log.d(TAG, "searchItem != null")
            val searchView = searchItem.actionView as SearchView
            val editext = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
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
                            val subject = it.department.toLowerCase()+"."+it.code
                            if (subject.contains(search) || it.name.toLowerCase().contains(search)) {
                                Log.d(TAG, "subject.contains(search)")
                                displayedSubjects.add(it)
                            }
                        }
                    } else {
                        displayedSubjects.addAll(subjects)
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

            }
            R.id.nav_courses -> {

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadData() {
        Log.d(TAG, "loadData")


        val service = ServiceVolley()
        val apiController = APIController(service)

        val path = "materias?carrera=1"

        apiController.get(path) { response ->
            Log.d(TAG, response.toString())
            if (response != null) {
                val jSONSubjects= response.getJSONArray("subjects")
                for (i in 0 until jSONSubjects.length()) {
                    subjects.add(Subject(jSONSubjects.getJSONObject(i).getString("name"),
                            jSONSubjects.getJSONObject(i).getString("code"),
                            jSONSubjects.getJSONObject(i).getString("department_code")
                    ))
                }
                subjects.sort()

                displayedSubjects.addAll(subjects)

                subjects_recycler_view.adapter!!.notifyDataSetChanged()
            }

        }

        /*subjects.add(Subject("Introduccion a los sistemas inteligentes", "12","71"))
        subjects.add(Subject("Modelos y Optimizacon II", "13","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "14","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "15","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "16","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "17","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "18","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "19","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "20","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "21","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "22","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "23","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "24","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "25","72"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "26","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "27","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "28","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "29","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "31","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "32","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "33","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "34","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "35","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "36","71"))
        subjects.add(Subject("Introduccion a los sistemas inteligentes", "37","71"))
        subjects.add(Subject("Administración y Control de Proyectos Informáticos II", "38","71"))*/

//        subjects.sort()

//        displayedSubjects.addAll(subjects)

//        subjects_recycler_view.adapter!!.notifyDataSetChanged()

    }
}