package com.sample.todo.retrofit.api

import com.sample.todo.model.authentication.TodoLoginRequestModel
import com.sample.todo.model.authentication.TodoLoginResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiInterface {

    @POST("api/login")
    suspend fun loginUser2(@Body data: TodoLoginRequestModel): Response<TodoLoginResponseModel>

}