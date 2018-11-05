package com.matacos.mataco.clases

class Career constructor(val code: String) : Comparable<Career> {

    override operator fun compareTo(other: Career): Int {
        return this.code.compareTo(this.code)
    }

    fun name(): String {
        return when (this.code.toInt()) {
            1 -> ("1    Ingenieria Civil")
            2 -> ("2    Ingenieria Industrial")
            3 -> ("3    Ingenieria Naval y Mecánica")
            4 -> ("4    Agrimensura")
            5 -> ("5    Ingenieria Mecánica")
            6 -> ("6    Ingenieria Electricista")
            7 -> ("7    Ingenieria Electrónica")
            8 -> ("8    Ingenieria Química")
            9 -> ("9    Licenciatura en Análisis de Sistemas")
            10 -> ("10   Ingenieria en Informatica")
            11 -> ("11   Ingenieria en Alimentos")
            else -> ("")
        }
    }

}
