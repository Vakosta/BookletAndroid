package com.booklet.bookletandroid.domain.repository

import android.content.Context
import com.booklet.bookletandroid.data.model.booklet.auth.Auth
import retrofit2.Response

class AuthRepository(context: Context) : BaseRepository(context) {
    suspend fun auth(
            diary: String,
            login: String,
            password: String
    ) = client.auth(diary, login, password)

    suspend fun auth(
            diary: String,
            login: String,
            password: String,
            region: Int?,
            province: Int?,
            city: Int?,
            school: Int?
    ): Response<Auth> = client.auth(diary, login, password, region, province, city, school)

    suspend fun getNetschoolData(region: Int?, province: Int?, city: Int?) =
            client.getNetschoolData(region, province, city)
}