package com.matacos.mataco.clases

data class ExamInscription(@SerializedName("classroom_code") val classroom_code: String,
                  @SerializedName("exam_date") val exam_date: String,
                  @SerializedName("beginning") val beginning: String,
                  @SerializedName("ending") val ending: String,
                  @SerializedName("classroom_campus") val classroom_campus: String,
                  @SerializedName("department_code") val department_code: String,
                  @SerializedName("subject_code") val subject_code: String,
                  val status: String = "Regular"): Comparable<ExamInscription> {

    override operator fun compareTo(other: ExamInscription): Int {
        //TODO: Por ahi conviene ordenar por fecha
        val thisSubject = this.department_code + this.subject_code
        val otherSubjet =  other.department_code + other.subject_code
        return thisSubject.compareTo(otherSubjet)
    }
}