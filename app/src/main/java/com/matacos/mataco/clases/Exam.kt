package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Exam(@SerializedName("examiner") val examiner: Examiner,
                  @SerializedName("id") val id: Int,
                  @SerializedName("classroom_code") val classroomCode: String,
                  @SerializedName("classroom_campus") val classroomCampus: String,
                  @SerializedName("beginning") val beginning: String,
                @SerializedName("ending") val ending: String,
                  @SerializedName("exam_date") val date: String,
                @SerializedName("enroled") val enroled: Boolean): Comparable<Exam> {

    override operator fun compareTo(other: Exam): Int {
        return this.id.compareTo(other.id)
    }

    fun classroomCampus(): String {
        return "Sede ${this.classroomCampus}"
    }

    fun id(): String {
        return "Cátedra ${this.id}"
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
        return formatDate(this.date)
    }
}
