package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class Subject(@SerializedName("name") val name: String,
                   @SerializedName("code") val code: String,
                   @SerializedName("department_code") val department: String,
                   @SerializedName("enroled") val enroled: Boolean,
                   @SerializedName("approved") val approved: Boolean): Comparable<Subject> {

    override operator fun compareTo(other: Subject): Int {
        val thisSubject = this.name + this.department + this.code
        val otherSubjet =  other.name + other.department + other.code
        return thisSubject.compareTo(otherSubjet)
    }

    fun subject(): String {
        return "${this.department}.${this.code}"
    }
}