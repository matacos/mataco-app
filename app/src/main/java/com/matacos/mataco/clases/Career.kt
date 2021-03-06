package com.matacos.mataco.clases

class Career constructor(val code: String) : Comparable<Career> {

    override operator fun compareTo(other: Career): Int {
        return this.code.compareTo(this.code)
    }

    fun career(): String {
        return when (this.code.toInt()) {
            1 -> ("Ingenieria Civil")
            2 -> ("Ingenieria Industrial")
            3 -> ("Ingenieria Naval y Mecánica")
            4 -> ("Agrimensura")
            5 -> ("Ingenieria Mecánica")
            6 -> ("Ingenieria Electricista")
            7 -> ("Ingenieria Electrónica")
            8 -> ("Ingenieria Química")
            9 -> ("Licenciatura en Análisis de Sistemas")
            10 -> ("Ingenieria en Informatica")
            11 -> ("Ingenieria en Alimentos")
            else -> ("")
        }
    }

    fun icon(): String {
        return "file:///android_asset/i_${this.code}.jpg"
    }

    override fun toString(): String {
        return "[$code] ${this.career()}"
    }
}
