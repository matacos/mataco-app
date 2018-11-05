package com.matacos.mataco.clases

class Career constructor(val code: String) : Comparable<Career> {

    override operator fun compareTo(other: Career): Int {
        return this.code.compareTo(this.code)
    }

    fun name(): String {
        return when (this.code.toInt()) {
            1 -> ("ngenieria Civil")
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

}
