package com.matacos.mataco

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.itextpdf.text.BadElementException
import com.itextpdf.text.Image
import com.matacos.mataco.clases.Career
import com.matacos.mataco.clases.NavigationItemManager
import kotlinx.android.synthetic.main.activity_certificate.*
import kotlinx.android.synthetic.main.app_bar_certificate.*
import kotlinx.android.synthetic.main.content_certificate.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CertificateActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = CertificateActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_certificate)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        download_certificate_button.setOnClickListener {
            Log.d(TAG, "clicked download_certificate_button")
            attemptSendingSurvey()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val navigationItemManager = NavigationItemManager()
        return navigationItemManager.navegate(this, item, preferences, drawer_layout)
    }

    private fun attemptSendingSurvey() {
        Log.d(TAG, "attemptSendingSurvey")
        checkPermissions()

        authority.error = null

        val authorityStr = authority.text.toString().trim()

        var cancel = false
        var focusView: View? = null

        // Check for a valid user address.
        if (TextUtils.isEmpty(authorityStr)) {
            authority.error = getString(R.string.error_authority_required)
            focusView = authority
            cancel = true
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {

            generatePDF()

        }
    }

    private fun checkPermissions() {
        Log.d(TAG, "checkPermissions")
        var checkPermission = false
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            checkPermission = true
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            checkPermission = true
        }
        if (checkPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        99)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                             permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            99 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(this, SubjectsSelectCareerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this.startActivity(intent)
                }
            }
        }
    }

    private fun generatePDF() {
        Log.d(TAG, "generatePDF")
        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val name: String = preferences.getString("name", "Santiago")
        val surname: String = preferences.getString("surname", "Gandolfo")
        val username: String = preferences.getString("username", "")
        val careers = ArrayList<Career>()
        val careerIds: MutableSet<String> = preferences.getStringSet("career_ids", null)
        for (careerId in careerIds) {
            val career = Career(careerId)
            careers.add(career)
        }
        careers.sort()


        val templatePDF = TemplatePDFK()
        templatePDF.openDocument()
        templatePDF.addMetaData("Certificado", "Alumno regular", "FIUBA")
        val today = getDate()
        templatePDF.addImage(addImagePDF())
        templatePDF.addTitles("Facultad de Ingeniería de la Universidad de Buenos Aires", "Certificado de Alumno Regular", today)
        templatePDF.addParagraph("Por la presente certifico que $name $surname, Padrón N° $username es alumno/a regular de la/s carrera/s de: ${careers.joinToString()}")
        templatePDF.addParagraph("A pedido del/la interesado/a, y al solo efecto de ser presentado ante las autoridades de: ${authority.text}.")
        templatePDF.addParagraph("Se expide el presente certificado en la Ciudad de Buenos Aires.")
        templatePDF.addParagraph("Intervino:")
        templatePDF.closeDocument()
        templatePDF.viewPDF(this)
    }

    private fun addImagePDF(): Image {
        Log.d(TAG, "addImagePDF")
        val d = ContextCompat.getDrawable(this, R.drawable.logo_fiuba)
        val bitDw = d as BitmapDrawable
        val bmp = bitDw.bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return Image.getInstance(stream.toByteArray())
    }

    fun getDate(): String {
        Log.d(TAG, "getDate")
        val currentTime = Calendar.getInstance()
        val year = currentTime.get(Calendar.YEAR)
        val month = currentTime.get(Calendar.MONTH) + 1
        val day = currentTime.get(Calendar.DAY_OF_MONTH)

        return "$day/$month/$year"
    }

}