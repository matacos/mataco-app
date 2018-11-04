package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class StudentRecord(@SerializedName("exam_date") val examDate: String,
                         @SerializedName("result") val result: String,
                         @SerializedName("subject_code") val subjectCode: String,
                         @SerializedName("department_code") val departmentCode: String,
                         @SerializedName("name") val name: String) : Comparable<StudentRecord> {

    override operator fun compareTo(other: StudentRecord): Int {
        return this.examDate.compareTo(other.examDate)
    }

    fun subject(): String {
        return "${this.departmentCode}.${this.subjectCode}"
    }

    fun date(): String{
        return formatDate(this.examDate)
    }

    fun result(): String{
        return "Nota: ${this.result}"
    }

    private fun formatDate(date: String): String {
        val realDate: Date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date.substring(0, 10))
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return format.format(realDate)
    }
}