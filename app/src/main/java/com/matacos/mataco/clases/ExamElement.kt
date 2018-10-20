package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class ExamElement(@SerializedName("exam") val exam: ExamInscription,
                       @SerializedName("enrolment_type") val state: String)
