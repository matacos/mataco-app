package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class SurveySubject(
        @SerializedName("semester") val semester: String,
        @SerializedName("subject_name") val name: String,
        @SerializedName("subject_code") val code: String,
        @SerializedName("department_code") val department: String,
        @SerializedName("course") val course: Int) : Comparable<SurveySubject> {

    override operator fun compareTo(other: SurveySubject): Int {
        val thisSubject: String = this.name + this.department + this.code
        val otherSubjet: String = other.name + other.department + other.code
        return thisSubject.compareTo(otherSubjet)
    }

    fun subject(): String {
        return "${this.department}.${this.code}"
    }

    fun semester(): String {
        return "Cuatrimestre ${this.semester.substring(0, 1)} de ${this.semester.substring(2)}"
    }

}