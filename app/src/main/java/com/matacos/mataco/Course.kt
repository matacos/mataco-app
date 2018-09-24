package com.matacos.mataco

data class Course(val name: String,
                  val department: String,
                  val code: String,
                  val totalSlots: String,
                  val professors: String): Comparable<Course> {
    override operator fun compareTo(other: Course): Int {
        var thisSubject = this.name + this.department + this.code
        var otherSubjet =  other.name + other.department + other.code
        return thisSubject.compareTo(otherSubjet)
    }
}