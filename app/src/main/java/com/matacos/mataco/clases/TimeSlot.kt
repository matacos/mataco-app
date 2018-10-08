package com.matacos.mataco.clases

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

data class TimeSlot(val classroomCode: String,
                    val classroomCampus: String,
                    val beginning: String,
                    val ending: String,
                    val dayOfWeek: String,
                    val description: String) : Comparable<TimeSlot> {
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