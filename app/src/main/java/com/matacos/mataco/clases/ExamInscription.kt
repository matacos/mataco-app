package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class ExamInscription(@SerializedName("classroom_code") val classroom_code: String,
                           @SerializedName("exam_date") val exam_date: String,
                           @SerializedName("beginning") val beginning: String,
                           @SerializedName("ending") val ending: String,
                           @SerializedName("classroom_campus") val classroom_campus: String,
                           @SerializedName("department_code") val department_code: String,
                           @SerializedName("subject_code") val subject_code: String,
                           @SerializedName("subject") val subject: Subject,
                           @SerializedName("examiner") val examiner: Examiner,
                           val status: String = "Regular"): Comparable<ExamInscription> {//TODO: Parsear de request

    override operator fun compareTo(other: ExamInscription): Int {
        //TODO: Por ahi conviene ordenar por fecha
        val thisSubject = this.department_code + this.subject_code
        val otherSubjet =  other.department_code + other.subject_code
        return thisSubject.compareTo(otherSubjet)
    }

    private fun formatDate(date: String): String {
        val realDate: Date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date.substring(0,10))
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return format.format(realDate)
    }

    private fun formatTime(date: String): String {
        val realDate: Date = SimpleDateFormat("HH:mm:ss", Locale.US).parse(date)
        val format = SimpleDateFormat("HH:mm", Locale.US)
        return format.format(realDate)
    }

    fun beginning(): String {
        return formatTime(this.beginning)
    }

    fun ending(): String {
        return formatTime(this.ending)
    }

    fun date():String{
        return formatDate(this.exam_date)
    }

    fun subject_name(): String{
        return subject.name
    }
}