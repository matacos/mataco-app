package com.matacos.mataco.apiController

import org.json.JSONObject

class APIController constructor(serviceInjection: ServiceInterface): ServiceInterface {
    private val service: ServiceInterface = serviceInjection

    override fun get(path: String, completionHandler: (response: JSONObject?) -> Unit) {
        service.get(path, completionHandler)
    }

    override fun get(path: String, token: String, completionHandler: (response: JSONObject?) -> Unit) {
        service.get(path, token, completionHandler)
    }
    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        service.post(path,params , completionHandler)
    }

    override fun post(path: String, token: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        service.post(path, token,  params, completionHandler)
    }

    override fun delete(path: String, token: String, completionHandler: (response: JSONObject?) -> Unit) {
        service.delete(path, token, completionHandler)
    }

    override fun put(path: String, token: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        service.put(path, token,  params, completionHandler)
    }
}