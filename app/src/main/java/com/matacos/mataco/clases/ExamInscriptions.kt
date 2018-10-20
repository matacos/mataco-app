package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class ExamInscriptions(@SerializedName("exam_enrolments") val examInscriptions: List<ExamElement> = listOf())