package com.matacos.mataco

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley

import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.lang.Integer.parseInt


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginActivity : AppCompatActivity() {

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
        if (preferences.getBoolean("logged_in", false)) {
            val intent = Intent(this, SubjectsSelectCareerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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
        val usernameStr = username.text.toString().trim()
        val passwordStr = password.text.toString().trim()

        var cancel = false
        var focusView: View? = null

        // Check for a valid user address.
        if (TextUtils.isEmpty(usernameStr)) {
            username.error = getString(R.string.error_username_required)
            focusView = username
            cancel = true
        } else if (!isUsernameValid(usernameStr)) {
            username.error = getString(R.string.error_invalid_user)
            focusView = username
            cancel = true
        } else if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_password_required)
            focusView = password
            cancel = true
        } else if (!isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
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

            val service = ServiceVolley()
            val apiController = APIController(service)

            val path = "api/login"
            val params = JSONObject()
            params.put("username", usernameStr)
            params.put("password", passwordStr)

            apiController.post(path, params) { response ->
                Log.d(TAG, response.toString())
                if (response != null) {


                    val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                    val editPreferences = preferences.edit()

                    editPreferences.putBoolean("logged_in", true).apply()
                    editPreferences.putString("username", usernameStr).apply()
                    editPreferences.putString("password", passwordStr).apply()
                    editPreferences.putString("token", response.getString("token")).apply()

                    val careers = response.getJSONObject("user").getJSONObject("rolesDescriptions").getJSONObject("students").getJSONArray("enrollments")
                    val careerIds = HashSet<String>()
                    for (i in 0 until careers.length()) {
                        Log.d(TAG, "AA: " + "career $i: " + careers.getJSONObject(i).getString("id"))
                        careerIds.add(careers.getJSONObject(i).getString("id"))
                    }
                    Log.d(TAG, "CareerIds: " + careerIds.toString())
                    editPreferences.putStringSet("career_ids", careerIds).apply()

                    showProgress(false)

                    getUserInformation(response.getString("token"))

                    sendFirebaseTokenToServer(usernameStr)

                    Log.d(TAG, "startActivity: SubjectsSelectCareerActivity")
                    val intent = Intent(this, SubjectsSelectCareerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this.startActivity(intent)

                } else {
                    showProgress(false)
                    Toast.makeText(this, "Esa combinación de padrón y contraseña no es correcta", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    private fun getUserInformation(token: String) {
        Log.d(TAG, "getUserInformation")

        val service = ServiceVolley()
        val apiController = APIController(service)

        val path = "api/me?"

        apiController.get(path, token) { response ->
            Log.d(TAG, response.toString())

            if (response != null) {
                val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                val editPreferences = preferences.edit()

                editPreferences.putString("name", response.getJSONObject("me").getString("name")).apply()
                editPreferences.putString("surname", response.getJSONObject("me").getString("surname")).apply()
                editPreferences.putString("email", response.getJSONObject("me").getString("email")).apply()
                editPreferences.putBoolean("regular", response.getJSONObject("me").getBoolean("regular")).apply()
            }
        }
    }

    private fun sendFirebaseTokenToServer(username: String) {
        Log.d(TAG, "sendFirebaseTokenToServer")
        val firebasePreferences:SharedPreferences = getSharedPreferences("my_firebase_preferences", Context.MODE_PRIVATE)
        val firebaseToken: String = firebasePreferences.getString("firebase_token", "")
        Log.d(TAG, "firebaseToken: $firebaseToken")
        if (firebaseToken != "") {
            val service = ServiceVolley()
            val apiController = APIController(service)

            val path = "api/firebase"
            val params = JSONObject()
            params.put("username", username)
            params.put("firebase_token", firebaseToken)

            apiController.post(path, params) { response ->
                if(response != null) {
                    Log.d(TAG, response.toString())
                }
            }
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        Log.d(TAG, "isUsernameValid")
        var numeric = true

        try {
            parseInt(username)
            if (!(5 <= username.length && username.length <= 6)) {
                numeric = false
            }
        } catch (e: NumberFormatException) {
            numeric = false
        }

        return numeric
    }

    private fun isPasswordValid(password: String): Boolean {
        Log.d(TAG, "isPasswordValid")
        return password.length >= 1
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


        login_progress.visibility = if (show) View.VISIBLE else View.INVISIBLE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.INVISIBLE
                    }
                })

    }

}