package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class StudentRecord(@SerializedName("exam") val exam: StudentRecordExam,
                         @SerializedName("grade") val result: Int) : Comparable<StudentRecord> {

    override operator fun compareTo(other: StudentRecord): Int {
        return this.exam.examDate.compareTo(other.exam.examDate)
    }

    fun subject(): String {
        return "${this.exam.departmentCode}.${this.exam.subjectCode}"
    }

    fun date(): String {
        return formatDate(this.exam.examDate)
    }

    fun result(): String {
        if (result >= 4) {
            return "Nota: ${this.result}"
        } else {
            return "Nota: 2 (insuf)"
        }
    }

    private fun formatDate(date: String): String {
        val realDate: Date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date.substring(0, 10))
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return format.format(realDate)
    }
}