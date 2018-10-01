package com.matacos.mataco

data class Course(val department_code: String,
                  val subject_code: String,
                  val subject_name: String,
                  val number: String,
                  val totalSlots: String,
                  val professors: String,
                  val classroomCampus: String,
                  val enrolled: Boolean,
                  val accepted: Boolean,
                  val timeSlots: ArrayList<TimeSlot>
                  ): Comparable<Course> {
    override operator fun compareTo(other: Course): Int {
        var thisSubject = this.number
        var otherSubjet =  this.number
        return thisSubject.compareTo(otherSubjet)
    }
}