package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class StudentRecords(@SerializedName("history_items") val studentRecords: List<StudentRecord> = listOf())