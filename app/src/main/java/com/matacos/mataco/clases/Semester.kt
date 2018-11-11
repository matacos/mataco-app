package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class Semester(@SerializedName("code") val code: String,
                  @SerializedName("ahora_consultan_cursos_disponibles") val canCheckAvailableCourses: Boolean,
                    @SerializedName("ahora_inscriben_cursos") val canSignUpCourses: Boolean,
                    @SerializedName("ahora_desinscriben_cursos") val canDropOutCourses: Boolean,
                    @SerializedName("ahora_inscriben_finales") val canViewExams: Boolean) {

    fun courses(): String {
        return "Oferta Académica Cuatrimestre ${this.code.substring(0,1)} de ${this.code.substring(2)}"
    }

    override fun toString(): String {
        return "Cuatrimestre ${this.code.substring(0,1)} de ${this.code.substring(2)}"
    }

}