package com.matacos.mataco

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley

import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.lang.Integer.parseInt


class LoginActivity : AppCompatActivity(){

    private val TAG: String = LoginActivity::class.java.simpleName



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        setContentView(R.layout.activity_login)
        // Set up the login form.

        user_sign_in_button.setOnClickListener {
            Log.d(TAG, "clicked user_sign_in_button")
            attemptLogin()
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        if(preferences.getBoolean("logged_in", false)){
            val intent = Intent(this, SubjectsActivity::class.java)
            this.startActivity(intent)
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid user, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        Log.d(TAG, "attemptLogin")
        // Reset errors.
        username.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val usernameStr = username.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid user address.
        if (TextUtils.isEmpty(usernameStr)) {
            username.error = getString(R.string.error_field_required)
            focusView = username
            cancel = true
        } else if (!isUsernameValid(usernameStr)) {
            username.error = getString(R.string.error_invalid_user)
            focusView = username
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)

            //TODO: Uncomment this
            /*
                       val service = ServiceVolley()
                       val apiController = APIController(service)



                                 val path = "login"
                                  val params = JSONObject()
                                  params.put("username", usernameStr)
                                  params.put("password", passwordStr)

                                  apiController.post(path, params) { response ->
                                      Log.d(TAG, response.toString())
                                      if (response != null) {
                                          Log.d(TAG, "startActivity: SubjectsActivity")

                                          //TODO: Si agregamos el tema de que se mande el errorJSONObject, deberíamos verificar el statusCode antes de seguir

                                          val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                                          val editPreferences = preferences.edit()

                                          editPreferences.putString("username", usernameStr).apply()
                                          editPreferences.putString("password", passwordStr).apply()
                                          editPreferences.putString("auth_token", response.getString("auth_token")).apply()
                                          editPreferences.putBoolean("logged_in", true).apply()

                                          showProgress(false)

                                          val intent = Intent(this, SubjectsActivity::class.java)
                                          this.startActivity(intent)
                                      }

                                      else {
                                          showProgress(false)
                                          Toast.makeText(this, "Error de inicio de sesión", Toast.LENGTH_SHORT).show()
                                      }

                                  }*/

            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val editPreferences = preferences.edit()
            editPreferences.putBoolean("logged_in", true).apply()
            showProgress(false)

            val intent = Intent(this, SubjectsActivity::class.java)
            this.startActivity(intent)

        }
    }


    private fun isUsernameValid(username: String): Boolean {
        Log.d(TAG, "isUsernameValid")
        var numeric = true

        try {
            parseInt(username)
        } catch (e: NumberFormatException) {
            numeric = false
        }
        return numeric
    }

    private fun isPasswordValid(password: String): Boolean {
        Log.d(TAG, "isPasswordValid")
        return password.length > 4
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        Log.d(TAG, "showProgress")
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })

    }

}
