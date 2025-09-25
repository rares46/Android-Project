package com.example.myapplication

import retrofit2.http.*

interface TodoApiService {
    @GET("todos/{id}")
    suspend fun getTodo(@Path("id") id: Int): Todo

    @PUT("todos/{id}")
    suspend fun updateTodo(@Path("id") id: Int, @Body todo: Todo): Todo
}
