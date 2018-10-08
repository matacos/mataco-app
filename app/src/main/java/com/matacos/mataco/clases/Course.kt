package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class Course(@SerializedName("department_code") val department_code: String,
                  @SerializedName("subject_code") val subject_code: String,
                  @SerializedName("subject_name") val subject_name: String,
                  @SerializedName("course") val number: String,
                  @SerializedName("free_slots") val totalSlots: String,
                  @SerializedName("professors") val professors: List<Professor>,
                  @SerializedName("enroled") val enrolled: Boolean = false,
                  @SerializedName("time_slots") val timeSlots: List<TimeSlot>,
                  val classroomCampus: String = "Paseo Colon",
                  val accepted: Boolean = true): Comparable<Course> {

    override operator fun compareTo(other: Course): Int {
        val thisSubject = this.number
        val otherSubjet =  this.number
        return thisSubject.compareTo(otherSubjet)
    }

    fun subject(): String {
        return "${this.department_code}.${this.subject_code}"
    }

    fun professors(): String {
        var professors = ""
        for (professor in this.professors) {
            professors += "${professor.toString()}, "
        }
        professors = professors.trim().trimEnd(',')
        return "Cátedra ${this.number} - ${professors}"
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






