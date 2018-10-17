package com.miramir.mahfaze.data.remote

import com.miramir.mahfaze.data.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserService {
    @FormUrlEncoded
    @POST("users")
    fun loginOrRegister(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<User>
}