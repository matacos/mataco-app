package com.matacos.mataco.clases

import com.google.gson.annotations.SerializedName

data class Courses(@SerializedName("courses") val courses: List<Course> = listOf())