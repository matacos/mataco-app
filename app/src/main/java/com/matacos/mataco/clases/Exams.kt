package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class Exams(@SerializedName("exams") val exams: List<Exam> = listOf())