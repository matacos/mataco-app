package com.matacos.mataco.apiController

import org.json.JSONObject

interface ServiceInterface {
    fun get(path: String, completionHandler: (response: JSONObject?) -> Unit)
    fun post(path: String,params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)

}