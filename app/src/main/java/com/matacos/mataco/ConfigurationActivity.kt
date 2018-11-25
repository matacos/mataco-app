package com.matacos.mataco

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import com.matacos.mataco.clases.NavigationItemManager
import kotlinx.android.synthetic.main.activity_configuration.*
import kotlinx.android.synthetic.main.app_bar_configuration.*
import kotlinx.android.synthetic.main.content_configuration.*
import org.json.JSONObject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ConfigurationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = ConfigurationActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_configuration)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        edit_password_button.setOnClickListener {
            Log.d(TAG, "clicked edit_password_button")
            attemptSendingSurvey()
        }
        cancel_button.setOnClickListener {
            Log.d(TAG, "clicked cancel_button")
            Log.d(TAG, "startActivity: SubjectsSelectCareerActivity")
            val intent = Intent(this, SubjectsSelectCareerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val navigationItemManager = NavigationItemManager()
        return navigationItemManager.navigate(this, item, preferences, drawer_layout)
    }

    private fun attemptSendingSurvey() {
        Log.d(TAG, "attemptSendingSurvey")

        new_password.error = null
        repeat_password.error = null

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        val oldPasswordStr: String = preferences.getString("password", "")

        // Store values at the time of the login attempt.
        val newPasswordStr = new_password.text.toString().trim()
        val repeatPasswordStr = repeat_password.text.toString().trim()

        var cancel = false
        var focusView: View? = null

        // Check for a valid user address.
        if (TextUtils.isEmpty(newPasswordStr)) {
            new_password.error = getString(R.string.error_new_password_required)
            focusView = new_password
            cancel = true
        } else if (TextUtils.isEmpty(repeatPasswordStr)) {
            repeat_password.error = getString(R.string.error_repeat_password_required)
            focusView = repeat_password
            cancel = true
        } else if (newPasswordStr != repeatPasswordStr) {
            repeat_password.error = getString(R.string.error_new_password_not_match)
            focusView = repeat_password
            cancel = true
        } else if (newPasswordStr == oldPasswordStr) {
            repeat_password.error = getString(R.string.error_new_password_same)
            focusView = repeat_password
            cancel = true
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {

            updatePassword(newPasswordStr)

        }
    }

    private fun updatePassword(newPassword: String) {
        val service = ServiceVolley()
        val apiController = APIController(service)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        val name: String = preferences.getString("name", "")
        val surname: String = preferences.getString("surname", "")
        val email: String = preferences.getString("email", "")
        val token: String = preferences.getString("token", "")

        val path = "api/me"
        val params = JSONObject()
        params.put("password", newPassword)
        params.put("name", name)
        params.put("surname", surname)
        params.put("email", email)



        apiController.put(path, token, params) { response ->
            Log.d(TAG, response.toString())
            if (response != null) {

                val editPreferences = preferences.edit()

                editPreferences.putString("password", newPassword).apply()

                Toast.makeText(this, "Contraseña actualizada con éxito!", Toast.LENGTH_SHORT).show()

                Log.d(TAG, "startActivity: SubjectsSelectCareerActivity")
                val intent = Intent(this, SubjectsSelectCareerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.startActivity(intent)

            } else {
                Toast.makeText(this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show()
            }

        }
    }

}