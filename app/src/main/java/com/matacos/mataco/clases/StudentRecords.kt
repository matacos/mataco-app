package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class StudentRecords(@SerializedName("student_records") val studentRecords: List<StudentRecord> = listOf())