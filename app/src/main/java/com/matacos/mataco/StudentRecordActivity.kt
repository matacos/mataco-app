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
import kotlinx.android.synthetic.main.content_student_record.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StudentRecordActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = StudentRecordActivity::class.java.simpleName
    val studentRecords = ArrayList<StudentRecord>()
    val displayedStudentRecords = ArrayList<StudentRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_record)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        student_record_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        student_record_recycler_view.adapter = StudentRecordAdapter(this, displayedStudentRecords, getSharedPreferences("my_preferences", Context.MODE_PRIVATE))

        swipe_refresh_content_student_record.setOnRefreshListener {
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
                    displayedStudentRecords.clear()
                    if (newText!!.isNotEmpty()) {
                        Log.d(TAG, "newText!!.isNotEmpty()")
                        val search = newText.toLowerCase()
                        studentRecords.forEach {
                            Log.d(TAG, "studentRecords.forEach")
                            if (it.subject().contains(search) || it.exam.subject.name.toLowerCase().contains(search)) {
                                Log.d(TAG, "studentRecords.contains(search)")
                                displayedStudentRecords.add(it)
                            }
                        }
                    } else {
                        displayedStudentRecords.addAll(studentRecords.distinct())
                    }
                    student_record_recycler_view.adapter!!.notifyDataSetChanged()
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

    private fun addEmptyListText(studentRecord:ArrayList<StudentRecord>) {
        Log.d(TAG, "addEmptyListText")
        if (studentRecord.isEmpty()) {
            no_available_student_record.visibility = View.VISIBLE
        }
    }

    private fun loadData() {
        Log.d(TAG, "loadData")

/*        studentRecords.add(StudentRecord("2017-06-04", "9", "06", "75", "Algoritmos y Programación I"))
        studentRecords.add(StudentRecord("2017-06-05", "8", "08", "61", "Álgebra II"))
        studentRecords.add(StudentRecord("2017-06-06", "insuf", "05", "75", "Algoritmos y Programación II"))

        studentRecords.sort()
        displayedStudentRecords.addAll(studentRecords)
        student_record_recycler_view.adapter!!.notifyDataSetChanged()*/

        studentRecords.clear()
        displayedStudentRecords.clear()

        student_record_recycler_view.adapter!!.notifyDataSetChanged()

        val service = ServiceVolley()
        val apiController = APIController(service)
        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token: String = preferences.getString("token", "")
        val username: String = preferences.getString("username", "")

        val path = "api/$username/historial_academico"

        apiController.get(path, token) { response ->
            Log.d(TAG, response.toString())

            if (response != null) {
                studentRecords.clear()
                displayedStudentRecords.clear()

                val gson = Gson()
                val gsonStudentRecords = gson.fromJson(response.toString(), StudentRecords::class.java)

                studentRecords.addAll(gsonStudentRecords.studentRecords)
                Log.d(TAG, "Date: " + studentRecords)
                for (studentRecord in studentRecords){
                    Log.d(TAG, "Date: " + studentRecord)
                }
                studentRecords.sort()
                displayedStudentRecords.addAll(studentRecords)
                addEmptyListText(studentRecords)

                student_record_recycler_view.adapter!!.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
            swipe_refresh_content_student_record.isRefreshing = false
        }
    }

}