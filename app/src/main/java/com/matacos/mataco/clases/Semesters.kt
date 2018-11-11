package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class Semesters(@SerializedName("semesters") val semesters: List<Semester> = listOf())