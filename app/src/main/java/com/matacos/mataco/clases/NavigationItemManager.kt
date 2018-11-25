package com.matacos.mataco.clases

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.matacos.mataco.*
import androidx.drawerlayout.widget.DrawerLayout

class NavigationItemManager {

    fun navigate(context: Context, item: MenuItem, preferences: SharedPreferences, drawer_layout: DrawerLayout): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_subjects -> {
                val intent = Intent(context, SubjectsSelectCareerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            R.id.nav_courses -> {
                val intent = Intent(context, MyCoursesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            R.id.nav_exam_subjects -> {
                val intent = Intent(context, ExamSubjectsSelectCareerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            R.id.nav_my_exams -> {
                val intent = Intent(context, MyExamsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            R.id.nav_student_record -> {
                val intent = Intent(context, StudentRecordActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            R.id.nav_surveys -> {
                val intent = Intent(context, SurveySubjectsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            R.id.nav_certificate -> {
                val intent = Intent(context, CertificateActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            R.id.nav_configuration -> {
                val intent = Intent(context, ConfigurationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            R.id.nav_log_out -> {
                val editPreferences: SharedPreferences.Editor = preferences.edit()
                editPreferences.clear().apply()
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}