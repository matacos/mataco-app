package com.matacos.mataco.clases

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
        val thisSubject = this.number
        val otherSubjet =  this.number
        return thisSubject.compareTo(otherSubjet)
    }

    fun subject(): String {
        return "${this.department_code}.${this.subject_code}"
    }

    fun professors(): String {
        return "Cátedra ${this.number} - ${this.professors}"
    }

    fun classroomCampus(): String {
        return "Sede ${this.classroomCampus}"
    }

    fun state(): String {
        return "Estado: " + if(this.accepted) "Regular" else "Condicional"
    }

    fun totalSlots(): String {
        return "Cupos ${this.totalSlots}"
    }

    fun number(): String {
        return "Cátedra ${this.number}"
    }
}






