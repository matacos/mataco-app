package com.matacos.mataco

data class Course(val number: String,
                  val totalSlots: String,
                  val professors: String,
                  val classroomCampus: String,
                  val timeSlots: ArrayList<TimeSlot>
                  ): Comparable<Course> {
    override operator fun compareTo(other: Course): Int {
        var thisSubject = this.number
        var otherSubjet =  this.number
        return thisSubject.compareTo(otherSubjet)
    }
}