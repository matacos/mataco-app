package com.matacos.mataco.apiController

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class ServiceVolley : ServiceInterface {
    private val TAG: String = ServiceVolley::class.java.simpleName
    val basePath = " https://mataco.herokuapp.com/"

    override fun get(path: String, completionHandler: (response: JSONObject?) -> Unit) {
        Log.d(TAG, "get")
        val definitePath = basePath + path
        val jsonObjReq = object : JsonObjectRequest(Method.GET, definitePath, null,
                Response.Listener<JSONObject> { response ->
                    Log.d(TAG, "/get request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/get request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun get(path: String, token: String, completionHandler: (response: JSONObject?) -> Unit) {
        Log.d(TAG, "get(with token)")
        val definitePath = basePath + path
        val jsonObjReq = object : JsonObjectRequest(Method.GET, definitePath, null,
                Response.Listener<JSONObject> { response ->
                    Log.d(TAG, "/get request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/get request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                headers.put("Authorization", "Bearer $token")
                return headers
            }
        }
        Log.d(TAG, "Sending to: $definitePath, with Headers: ${jsonObjReq.headers}")
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        Log.d(TAG, "post")
        val definitePath = basePath + path
        val jsonObjReq = object : JsonObjectRequest(Method.POST, definitePath, params,
                Response.Listener<JSONObject> { response ->
                    Log.d(TAG, "/post request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }
        Log.d(TAG, "Sending to: $definitePath, with Headers: ${jsonObjReq.headers}")
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }
}