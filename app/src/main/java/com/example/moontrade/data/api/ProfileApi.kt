package com.example.moontrade.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import com.example.moontrade.model.ProfileUpdateRequest

interface ProfileApi {

    @Multipart
    @POST("/upload/avatar")
    suspend fun uploadAvatar(
        /* NO part name here — MultipartBody.Part already contains it */
        @Part file: MultipartBody.Part,
        @Header("Authorization") auth: String
    ): Response<String>

    @PATCH("/profile/self")
    suspend fun updateProfile(
        @Body profile: ProfileUpdateRequest,
        @Header("Authorization") auth: String
    ): Response<Unit>
}
