package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class TimeSlot(@SerializedName("classroom_code") val classroomCode: String,
                    @SerializedName("classroom_campus") val classroomCampus: String,
                    @SerializedName("beginning") val beginning: String,
                    @SerializedName("ending") val ending: String,
                    @SerializedName("day_of_week") val dayOfWeek: String,
                    @SerializedName("description") val description: String) : Comparable<TimeSlot> {
    override operator fun compareTo(other: TimeSlot): Int {
        val thisTimeSlot = this.dayOfWeek + this.beginning
        val otherTimeSlot = other.dayOfWeek + other.beginning
        return thisTimeSlot.compareTo(otherTimeSlot)
    }

    private fun formatDate(date: String): String {
        val realDate: Date = SimpleDateFormat("HH:mm:ss", Locale.US).parse(date)
        val format = SimpleDateFormat("HH:mm", Locale.US)
        return format.format(realDate)
    }

    fun beginning(): String {
        return formatDate(this.beginning)
    }

    fun ending(): String {
        return formatDate(this.ending)
    }

}