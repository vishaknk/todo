package com.sample.todo.retrofit.base


data class Error(
    var ErrorCode: String,
    var ErrorDescription: String,
    var MoreInfo: MoreInfo,
    var httpcode: Int
)