package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class StudentRecordExam(@SerializedName("exam_date") val examDate: String,
                             @SerializedName("subject_code") val subjectCode: String,
                             @SerializedName("department_code") val departmentCode: String,
                             @SerializedName("subject") val subject: StudentRecordSubject)