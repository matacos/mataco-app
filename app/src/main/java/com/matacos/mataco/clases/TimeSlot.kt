package com.matacos.mataco.clases

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
}