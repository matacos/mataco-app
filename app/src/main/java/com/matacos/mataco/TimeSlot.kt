package com.matacos.mataco

data class TimeSlot(val classroomCode: String,
                  val classroomCampus: String,
                  val beginning: String,
                  val ending: String,
                  val dayOfWeek: String): Comparable<TimeSlot> {
    override operator fun compareTo(other: TimeSlot): Int {
        var thisTimeSlot = this.dayOfWeek + this.beginning
        var otherTimeSlot =  other.dayOfWeek + other.beginning
        return thisTimeSlot.compareTo(otherTimeSlot)
    }
}