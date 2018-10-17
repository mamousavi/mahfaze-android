package com.miramir.mahfaze.data.remote

import com.miramir.mahfaze.data.model.Note
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NoteService {
    @GET("notes")
    fun getAll(): Call<List<Note>>

    @POST("notes/batch")
    fun batchQuery(@Body request: NoteBatchQueryRequest): Call<Unit>
}