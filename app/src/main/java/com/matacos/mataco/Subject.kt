package com.matacos.mataco

data class Subject(val name: String, val code: String,val department: String): Comparable<Subject> {
    override operator fun compareTo(other: Subject): Int {
        var thisSubject = this.name + this.department + this.code
        var otherSubjet =  other.name + other.department + other.code
        return thisSubject.compareTo(otherSubjet)
    }
}