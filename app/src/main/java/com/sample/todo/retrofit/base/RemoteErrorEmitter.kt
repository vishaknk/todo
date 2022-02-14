package com.sample.todo.retrofit.base

interface RemoteErrorEmitter {
    fun onError(errorType: ErrorType, msg: String)
}