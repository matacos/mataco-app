package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class Professor(@SerializedName("name") val name: String,
                     @SerializedName("surname") val surname: String) {

    override fun toString(): String {
        return "${this.name} ${this.surname}"
    }

}