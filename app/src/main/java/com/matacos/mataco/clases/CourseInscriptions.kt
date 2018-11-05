package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class CourseInscriptions(@SerializedName("courseInscriptions") val courses: List<CoursesInscription> = listOf())