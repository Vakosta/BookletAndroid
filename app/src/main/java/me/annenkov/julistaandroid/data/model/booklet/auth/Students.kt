package me.annenkov.julistaandroid.data.model.booklet.auth

import com.google.gson.annotations.SerializedName

data class Students(
        @SerializedName("default")
        val jsonMemberDefault: Int? = null,

        @SerializedName("list")
        val list: List<Student>? = null
)