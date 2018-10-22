package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class Subjects(@SerializedName("subjects") val subjects: List<Subject> = listOf())