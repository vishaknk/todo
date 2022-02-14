package com.sample.todo.model.authentication

import com.google.gson.annotations.SerializedName

data class TodoLoginResponseModel(
    @SerializedName("token") var token: String? = "",
)