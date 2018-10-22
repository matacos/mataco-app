package com.matacos.mataco

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import org.json.JSONObject


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    private val TAG: String = MyFirebaseInstanceIDService::class.java.simpleName

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed firebase token: " + refreshedToken!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editPreferences = preferences.edit()
        editPreferences.putString("firebase_token", refreshedToken).apply()

        val username: String = preferences.getString("username", "")
        if (username != "") {
            sendRegistrationToServer(username, refreshedToken)
        }


    }

    private fun sendRegistrationToServer(username: String, firebaseToken: String) {
        Log.d(TAG, "sendRegistrationToServer")
        val service = ServiceVolley()
        val apiController = APIController(service)

        val path = "api/firebase"
        val params = JSONObject()
        params.put("username", username)
        params.put("firebase_token", firebaseToken)
        Log.d(TAG, "Params: " + params.toString())
        apiController.post(path, params) { response ->
            Log.d(TAG, response.toString())
        }
    }
}