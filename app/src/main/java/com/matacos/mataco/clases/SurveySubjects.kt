package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class SurveySubjects(@SerializedName("courses") val subjects: List<SurveySubject> = listOf())