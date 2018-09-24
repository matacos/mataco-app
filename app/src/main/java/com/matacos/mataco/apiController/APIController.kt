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
    override fun post(path: String, params: JSONObject,completionHandler: (response: JSONObject?) -> Unit) {
        service.post(path,params , completionHandler)
    }
}