package com.booklet.bookletandroid.data.model.booklet.auth

import com.google.gson.annotations.SerializedName

data class Student(
        @SerializedName("school_id")
        val schoolId: Long? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("school_name")
        val schoolName: String? = null,

        @SerializedName("id")
        val id: Long? = null,

        @SerializedName("class")
        val jsonMemberClass: String? = null
)