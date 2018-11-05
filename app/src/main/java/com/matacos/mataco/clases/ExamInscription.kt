package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class ExamInscription(@SerializedName("examiner") val examiner: Professor,
                           @SerializedName("id") val id: Int,
                           @SerializedName("classroom_code") val classroom_code: String,
                           @SerializedName("classroom_campus") val classroom_campus: String,
                           @SerializedName("beginning") val beginning: String,
                           @SerializedName("ending") val ending: String,
                           @SerializedName("exam_date") val exam_date: String,
                           @SerializedName("department_code") val department_code: String,
                           @SerializedName("subject_code") val subject_code: String,
                           @SerializedName("subject") val subject: Subject,
                           var status: String): Comparable<ExamInscription> {

    /*
    data class Exam(@SerializedName("examiner") val examiner: Professor,
                @SerializedName("id") val id: Int,
                @SerializedName("classroom_code") val classroomCode: String,
                @SerializedName("classroom_campus") val classroomCampus: String,
                @SerializedName("beginning") val beginning: String,
                @SerializedName("ending") val ending: String,
                @SerializedName("exam_date") val date: String,
                @SerializedName("enroled") val enroled: Boolean) : Comparable<Exam> {
    * */


    override operator fun compareTo(other: ExamInscription): Int {
        return this.exam_date.compareTo(other.exam_date)
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

    fun status():String{
        return status.capitalize()
    }
}